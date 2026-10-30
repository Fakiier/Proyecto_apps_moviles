package com.example.appcloro_grupojrcc.data.model

enum class TipoDesviacion { INSUFICIENTE, EXCESIVO }

data class Alerta(
    val id: Int,
    val puntoId: Int,
    val medicionId: Int,
    val gravedad: EstadoCloro, //advertencia o critico
    val tipo: TipoDesviacion,
    val fecha: String,
    val abierta: Boolean = true
)