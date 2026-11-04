package com.example.appcloro_grupojrcc.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertaDao {

    @Query("SELECT * FROM alertas ORDER BY fechaMillis DESC")
    fun todas(): Flow<List<AlertaEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(alerta: AlertaEntity): Long

    @Query("UPDATE alertas SET abierta = 0, cierreMillis = :cierre, cerradaPor = :por WHERE id = :id")
    suspend fun cerrar(id: Int, cierre: Long, por: String)


}