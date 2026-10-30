package com.example.appcloro_grupojrcc.data.model

data class PuntoMedicion(
    val id: Int,
    val codigo: String,       // ej: "GN-01"
    val granja: String,
    val nombre: String,       // ubicación, ej: "Galpón 1"
    val tipo: String,         // Bebedero, Tanque o Línea
    val nivelCloro: Double,   // cloro libre en ppm
    val actualizado: String   // fecha y hora de la última medición
) {
    val estado: EstadoCloro
        get() = calcularEstado(nivelCloro)
}