package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.model.Medicion
import kotlinx.coroutines.flow.Flow

interface MedicionesRepository {
    fun historial(puntoId: Int): Flow<List<Medicion>>
    fun historial(puntoId: Int, desdeMillis: Long): Flow<List<Medicion>>
    fun ultimas(): Flow<List<Medicion>>
    fun cantidadPendientes(): Flow<Int>
    suspend fun registrar(medicion: Medicion)
    suspend fun inicializar()
}