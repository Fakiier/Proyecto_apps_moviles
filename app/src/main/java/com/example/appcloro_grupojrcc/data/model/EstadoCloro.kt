package com.example.appcloro_grupojrcc.data.model

enum class EstadoCloro { NORMAL, ADVERTENCIA, CRITICO }

//Rangos en ppm
const val CLORO_CRITICO_MIN = 2.0
const val CLORO_NORMAL_MIN = 3.0
const val CLORO_NORMAL_MAX = 5.0
const val CLORO_CRITICO_MAX = 6.0

fun calcularEstado(nivel: Double): EstadoCloro = when {
    nivel < CLORO_CRITICO_MIN || nivel > CLORO_CRITICO_MAX -> EstadoCloro.CRITICO
    nivel < CLORO_NORMAL_MIN || nivel > CLORO_NORMAL_MAX -> EstadoCloro.ADVERTENCIA
    else -> EstadoCloro.NORMAL
}