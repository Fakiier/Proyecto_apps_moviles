package com.example.appcloro_grupojrcc.data.model

data class Medicion(
    val id: Int,
    val puntoId: Int,
    val nivelCloro: Double,
    val fecha: String,
    val registradoPor: String,
    val sincronizada: Boolean = true
) {
    val estado: EstadoCloro
        get() = calcularEstado(nivelCloro)
}