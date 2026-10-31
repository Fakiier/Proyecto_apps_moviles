package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.local.MedicionDao
import com.example.appcloro_grupojrcc.data.local.sembrarSiVacio
import com.example.appcloro_grupojrcc.data.local.toEntity
import com.example.appcloro_grupojrcc.data.local.toModel
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.PuntoMedicion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicionesRepositoryRoom(
    private val dao: MedicionDao,
    private val puntosIniciales: List<PuntoMedicion>
) : MedicionesRepository {

    override fun historial(puntoId: Int): Flow<List<Medicion>> = historial(puntoId, 0L)

    override fun historial(puntoId: Int, desdeMillis: Long): Flow<List<Medicion>> =
        dao.historial(puntoId, desdeMillis).map { lista -> lista.map { it.toModel() } }

    override fun ultimas(): Flow<List<Medicion>> =
        dao.ultimasPorPunto().map { lista -> lista.map { it.toModel() } }

    override fun cantidadPendientes(): Flow<Int> = dao.cantidadPendientes()

    override suspend fun registrar(medicion: Medicion) {
        dao.insertar(medicion.toEntity())
    }

    override suspend fun inicializar() = sembrarSiVacio(dao, puntosIniciales)
}