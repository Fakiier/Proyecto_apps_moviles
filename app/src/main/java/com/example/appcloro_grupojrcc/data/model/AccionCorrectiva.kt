package com.example.appcloro_grupojrcc.data.model

enum class EstadoResolucion { PENDIENTE, RESUELTA }

data class AccionCorrectiva(
    val id: Int,
    val alertaId: Int,
    val puntoId: Int,
    val medicionId: Int,
    val descripcion: String,
    val responsable: String,
    val fecha: String,
    val estado: EstadoResolucion = EstadoResolucion.PENDIENTE
)