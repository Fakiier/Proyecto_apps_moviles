package com.example.appcloro_grupojrcc.data.local
import com.example.appcloro_grupojrcc.data.model.PuntoMedicion
import kotlin.math.roundToInt
import kotlin.random.Random

// Genera 30 días de historial ficticio (1 medición cada 3 horas por punto)
// solo si la tabla está vacía. La última medición coincide con el nivel actual del punto.
suspend fun sembrarSiVacio(dao: MedicionDao, puntos: List<PuntoMedicion>) {
    if (dao.contar() > 0) return

    val tresHoras = 3 * 60 * 60 * 1000L
    val ahora = System.currentTimeMillis() / 60000 * 60000

    val filas = puntos.flatMap { p ->
        val azar = Random(p.id)
        (239 downTo 0).map { i ->
            val nivel = if (i == 0) p.nivelCloro
            else ((p.nivelCloro + azar.nextDouble(-0.6, 0.6)) * 100).roundToInt() / 100.0
            MedicionEntity(
                puntoId = p.id,
                nivelCloro = maxOf(nivel, 0.1),
                fechaMillis = ahora - i * tresHoras,
                registradoPor = "Sistema (simulado)"
            )
        }
    }
    dao.insertarTodas(filas)
}