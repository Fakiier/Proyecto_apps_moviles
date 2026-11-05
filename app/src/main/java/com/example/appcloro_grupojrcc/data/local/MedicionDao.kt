package com.example.appcloro_grupojrcc.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicionDao {

    @Query("SELECT * FROM mediciones WHERE puntoId = :puntoId AND fechaMillis >= :desde ORDER BY fechaMillis DESC")
    fun historial(puntoId: Int, desde: Long): Flow<List<MedicionEntity>>

    @Query("SELECT * FROM mediciones m WHERE fechaMillis = (SELECT MAX(fechaMillis) FROM mediciones WHERE puntoId = m.puntoId)")
    fun ultimasPorPunto(): Flow<List<MedicionEntity>>

    @Query("SELECT * FROM mediciones WHERE sincronizada = 0 ORDER BY fechaMillis")
    suspend fun pendientes(): List<MedicionEntity>

    @Query("SELECT COUNT(*) FROM mediciones WHERE sincronizada = 0")
    fun cantidadPendientes(): Flow<Int>

    @Query("SELECT COUNT(*) FROM mediciones")
    suspend fun contar(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(medicion: MedicionEntity): Long

    @Insert
    suspend fun insertarTodas(mediciones: List<MedicionEntity>)

    // Para lo que llega del servidor: ignora lo que ya existe
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarIgnorando(mediciones: List<MedicionEntity>)

    @Query("UPDATE mediciones SET sincronizada = 1 WHERE id IN (:ids)")
    suspend fun marcarSincronizadas(ids: List<Int>)
}