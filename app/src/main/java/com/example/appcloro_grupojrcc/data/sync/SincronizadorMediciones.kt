package com.example.appcloro_grupojrcc.data.sync

import com.example.appcloro_grupojrcc.data.local.MedicionDao
import com.example.appcloro_grupojrcc.data.local.MedicionEntity
import com.example.appcloro_grupojrcc.data.remote.CloroApi
import com.example.appcloro_grupojrcc.data.remote.NuevaMedicionRemota
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import java.io.IOException

sealed interface ResultadoSync {
    data class Exito(val subidas: Int, val nuevas: Int) : ResultadoSync
    data object SinConexion : ResultadoSync
    data class Error(val mensaje: String) : ResultadoSync
}

class SincronizadorMediciones(
    private val dao: MedicionDao,
    private val api: CloroApi,
    private val prefs: PreferenciasSync
) {
    private val candado = Mutex()

    suspend fun sincronizar(): ResultadoSync = candado.withLock {
        try {
            val subidas = subirPendientes()
            val nuevas = bajarNuevas()
            ResultadoSync.Exito(subidas, nuevas)
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            ResultadoSync.SinConexion
        } catch (e: HttpException) {
            ResultadoSync.Error("El servidor respondió ${e.code()}")
        } catch (e: Exception) {
            ResultadoSync.Error(e.message ?: "Error al sincronizar")
        }
    }

    // Sube las mediciones registradas sin conexión y las marca como sincronizadas
    private suspend fun subirPendientes(): Int {
        val pendientes = dao.pendientes()
        if (pendientes.isEmpty()) return 0
        api.subir(
            pendientes.map {
                NuevaMedicionRemota(it.puntoId, it.nivelCloro, it.fechaMillis, it.registradoPor)
            }
        )
        dao.marcarSincronizadas(pendientes.map { it.id })
        return pendientes.size
    }

    // Baja las mediciones del servidor posteriores a la última que se recibió
    private suspend fun bajarNuevas(): Int {
        val remotas = api.mediciones(prefs.ultimaMedicionRemota())
        val antes = dao.contar()
        if (remotas.isNotEmpty()) {
            dao.insertarIgnorando(
                remotas.map {
                    MedicionEntity(
                        puntoId = it.puntoId,
                        nivelCloro = it.nivelCloro,
                        fechaMillis = it.fechaMillis,
                        registradoPor = it.registradoPor,
                        sincronizada = true
                    )
                }
            )
        }
        prefs.guardar(remotas.maxOfOrNull { it.fechaMillis }, System.currentTimeMillis())
        return dao.contar() - antes
    }
}