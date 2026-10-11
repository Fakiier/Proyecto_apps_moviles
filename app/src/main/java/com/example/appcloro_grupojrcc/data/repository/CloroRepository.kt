package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.model.PuntoMedicion

class CloroRepository{
    fun obtenerPuntos(): List<PuntoMedicion> = listOf(
        PuntoMedicion(1, "Granja Norte", "Galpón 1", 0.9, "11-10-2026 08:30"),
        PuntoMedicion(2, "Granja Sur", "Galpon 2", 0.4, "11-10-2026 09:32")
        //datos de ejemplo
    )
}