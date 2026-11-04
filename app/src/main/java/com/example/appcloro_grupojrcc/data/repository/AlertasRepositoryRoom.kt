package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.local.AccionDao
import com.example.appcloro_grupojrcc.data.local.AlertaDao
import com.example.appcloro_grupojrcc.data.local.AlertaEntity
import com.example.appcloro_grupojrcc.data.local.fechaAMillis
import com.example.appcloro_grupojrcc.data.local.toEntity
import com.example.appcloro_grupojrcc.data.local.toModel
import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva
import com.example.appcloro_grupojrcc.data.model.Alerta
import com.example.appcloro_grupojrcc.data.model.CLORO_NORMAL_MIN
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.data.model.EstadoResolucion
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.TipoDesviacion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlertasRepositoryRoom(
    private val alertaDao: AlertaDao,
    private val accionDao: AccionDao
) : AlertasRepository {
    override fun alertas(): Flow<List<Alerta>> =
        alertaDao.todas().map { lista -> lista.map { it.toModel() } }

    override fun acciones(): Flow<List<AccionCorrectiva>> =
        accionDao.todas().map { lista -> lista.map { it.toModel() } }

    override suspend fun generarSiCorresponde(medicion: Medicion): Boolean {
        if (medicion.estado == EstadoCloro.NORMAL) return false
        val tipo = if (medicion.nivelCloro < CLORO_NORMAL_MIN) TipoDesviacion.INSUFICIENTE
        else TipoDesviacion.EXCESIVO
        val id = alertaDao.insertar(
            AlertaEntity(
                puntoId = medicion.puntoId,
                medicionId = medicion.id,
                gravedad = medicion.estado,
                tipo = tipo,
                fechaMillis = fechaAMillis(medicion.fecha)
            )
        )
        return id != -1L
    }

    override suspend fun registrarAccion(accion: AccionCorrectiva) {
        accionDao.insertar(accion.toEntity())
    }

    override suspend fun cerrar(alertaId: Int, por: String) {
        alertaDao.cerrar(alertaId, System.currentTimeMillis(), por)
        accionDao.marcarDeAlerta(alertaId, EstadoResolucion.RESUELTA)
    }
}