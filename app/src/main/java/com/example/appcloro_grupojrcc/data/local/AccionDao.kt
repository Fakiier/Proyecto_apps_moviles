package com.example.appcloro_grupojrcc.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appcloro_grupojrcc.data.model.EstadoResolucion
import kotlinx.coroutines.flow.Flow

@Dao
interface AccionDao {

    @Insert
    suspend fun insertar(accion: AccionEntity): Long

    @Query("SELECT * FROM acciones ORDER BY id DESC")
    fun todas(): Flow<List<AccionEntity>>

    @Query("UPDATE acciones SET estado = :estado WHERE alertaId = :alertaId")
    suspend fun marcarDeAlerta(alertaId: Int, estado: EstadoResolucion)
}