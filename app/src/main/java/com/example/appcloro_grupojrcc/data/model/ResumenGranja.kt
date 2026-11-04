package com.example.appcloro_grupojrcc.data.model

data class ResumenGranja(
    val granja: String,
    val puntos: Int,
    val normales: Int,
    val advertencias: Int,
    val criticos: Int,
    val promedio: Double
)

fun resumenPorGranja(
    puntos: List<PuntoMedicion>,
    ultimas: List<Medicion>
): List<ResumenGranja> =
    puntos.groupBy { it.granja }.map { (granja, lista) ->
        val niveles = lista.map { p ->
            ultimas.find { it.puntoId == p.id }?.nivelCloro ?: p.nivelCloro
        }
        val estados = niveles.map { calcularEstado(it) }
        ResumenGranja(
            granja = granja,
            puntos = lista.size,
            normales = estados.count { it == EstadoCloro.NORMAL },
            advertencias = estados.count { it == EstadoCloro.ADVERTENCIA },
            criticos = estados.count { it == EstadoCloro.CRITICO },
            promedio = niveles.average()
        )
    }