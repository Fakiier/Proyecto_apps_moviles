package com.example.appcloro_grupojrcc.data.model

data class PuntoMedicion(
    val id: Int,
    val granja: String,
    val nombre: String,  //por ejemplo "Galpón 2"
    val nivelCloro: Double,
    val actualizado: String //fecha y hora de la ultima medicion
) {
    val estado: EstadoCloro
        get() = when {
            nivelCloro < 0.2 || nivelCloro > 2.0 -> EstadoCloro.CRITICO
            nivelCloro < 0.5 || nivelCloro > 1.5 -> EstadoCloro.ADVERTENCIA
            else -> EstadoCloro.NORMAL
            //Los rangos son de ejemplo, se cambian cuando sepamos los reales omm
        }
}