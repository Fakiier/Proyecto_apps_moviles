package com.example.appcloro_grupojrcc.data.model
enum class RangoHistorial(val etiqueta: String, private val horas: Long) {
    DIA("24 h", 24L),
    SEMANA("7 días", 24L * 7),
    MES("30 días", 24L * 30);

    fun desdeMillis(): Long = System.currentTimeMillis() - horas * 3_600_000L
}