package com.example.appcloro_grupojrcc.data.local
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appcloro_grupojrcc.data.model.Medicion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "mediciones", indices = [Index("puntoId", "fechaMillis")])
data class MedicionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val puntoId: Int,
    val nivelCloro: Double,
    val fechaMillis: Long,
    val registradoPor: String,
    val sincronizada: Boolean = true
)

private const val FORMATO_FECHA = "dd-MM-yyyy HH:mm"

fun fechaAMillis(fecha: String): Long =
    runCatching { SimpleDateFormat(FORMATO_FECHA, Locale.getDefault()).parse(fecha)!!.time }
        .getOrDefault(System.currentTimeMillis())

fun millisAFecha(millis: Long): String =
    SimpleDateFormat(FORMATO_FECHA, Locale.getDefault()).format(Date(millis))

fun MedicionEntity.toModel() =
    Medicion(id, puntoId, nivelCloro, millisAFecha(fechaMillis), registradoPor, sincronizada)

fun Medicion.toEntity() =
    MedicionEntity(id, puntoId, nivelCloro, fechaAMillis(fecha), registradoPor, sincronizada)