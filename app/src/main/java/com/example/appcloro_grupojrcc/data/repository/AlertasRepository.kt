package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva
import com.example.appcloro_grupojrcc.data.model.Alerta
import com.example.appcloro_grupojrcc.data.model.Medicion
import kotlinx.coroutines.flow.Flow

interface AlertasRepository {
    fun alertas(): Flow<List<Alerta>>
    fun acciones(): Flow<List<AccionCorrectiva>>
    suspend fun generarSiCorresponde(medicion: Medicion): Boolean
    suspend fun registrarAccion(accion: AccionCorrectiva)
    suspend fun cerrar(alertaId: Int, por: String)
}