package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.model.Medicion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface MedicionesRepository {
    fun historial(puntoId: Int): Flow<List<Medicion>>
    suspend fun registrar(medicion: Medicion)
}

class MedicionesRepositoryFake : MedicionesRepository {
    private val lista = MutableStateFlow(
        listOf(
            Medicion(1, 1, 3.80, "22-10-2026 09:00", "Carlos Rojas"),
            Medicion(2, 1, 3.50, "22-10-2026 08:00", "Carlos Rojas"),
            Medicion(3, 5, 1.70, "22-10-2026 09:05", "Ana Pérez"),
            Medicion(4, 5, 2.40, "22-10-2026 08:05", "Ana Pérez")
        )
    )

    override fun historial(puntoId: Int) =
        lista.map { todas -> todas.filter { it.puntoId == puntoId } }

    override suspend fun registrar(medicion: Medicion) {
        lista.value = lista.value + medicion
    }
}
