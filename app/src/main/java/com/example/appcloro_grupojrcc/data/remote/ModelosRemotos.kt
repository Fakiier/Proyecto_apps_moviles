package com.example.appcloro_grupojrcc.data.remote

data class MedicionRemota(
    val id: Long,
    val puntoId: Int,
    val nivelCloro: Double,
    val fechaMillis: Long,
    val registradoPor: String
)

data class NuevaMedicionRemota(
    val puntoId: Int,
    val nivelCloro: Double,
    val fechaMillis: Long,
    val registradoPor: String
)