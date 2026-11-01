package com.example.appcloro_grupojrcc.data.model

data class ResumenMediciones(
    val cantidad: Int,
    val promedio: Double,
    val minimo: Double,
    val maximo: Double
)

fun List<Medicion>.resumen(): ResumenMediciones? {
    if (isEmpty()) return null
    val niveles = map { it.nivelCloro }
    return ResumenMediciones(size, niveles.average(), niveles.min(), niveles.max())
}