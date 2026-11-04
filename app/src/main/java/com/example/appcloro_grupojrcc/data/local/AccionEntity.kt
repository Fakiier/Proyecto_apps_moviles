package com.example.appcloro_grupojrcc.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva
import com.example.appcloro_grupojrcc.data.model.EstadoResolucion

@Entity(tableName = "acciones", indices = [Index("alertaId")])
data class AccionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val alertaId: Int,
    val puntoId: Int,
    val medicionId: Int,
    val descripcion: String,
    val responsable: String,
    val fecha: String,
    val estado: EstadoResolucion = EstadoResolucion.PENDIENTE,
    val fotoUri: String? = null
)

fun AccionEntity.toModel() = AccionCorrectiva(
    id, alertaId, puntoId, medicionId, descripcion, responsable, fecha, estado, fotoUri
)

fun AccionCorrectiva.toEntity() = AccionEntity(
    id, alertaId, puntoId, medicionId, descripcion, responsable, fecha, estado, fotoUri
)