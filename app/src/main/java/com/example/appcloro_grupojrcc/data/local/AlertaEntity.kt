package com.example.appcloro_grupojrcc.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appcloro_grupojrcc.data.model.Alerta
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.data.model.TipoDesviacion

@Entity(
    tableName = "alertas",
    indices = [Index("puntoId"), Index(value = ["medicionId"], unique = true)]
)

data class AlertaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val puntoId: Int,
    val medicionId: Int,
    val gravedad: EstadoCloro,
    val tipo: TipoDesviacion,
    val fechaMillis: Long,
    val abierta: Boolean = true,
    val cierreMillis: Long? = null,
    val cerradaPor: String? = null
)

fun AlertaEntity.toModel() = Alerta(
    id, puntoId, medicionId, gravedad, tipo,
    millisAFecha(fechaMillis), abierta, fechaMillis, cerradaPor
)