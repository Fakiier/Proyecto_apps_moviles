package com.example.appcloro_grupojrcc.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcloro_grupojrcc.data.local.AppDatabase
import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva
import com.example.appcloro_grupojrcc.data.model.Alerta
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.data.repository.AlertasRepository
import com.example.appcloro_grupojrcc.data.repository.AlertasRepositoryRoom
import com.example.appcloro_grupojrcc.data.repository.CloroRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepositoryRoom
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Una alerta lista para mostrar: cuántas acciones tiene y por qué no se puede cerrar
data class AlertaUi(val alerta: Alerta, val acciones: Int, val bloqueo: String?)

class AlertasViewModel(
    private val alertasRepo: AlertasRepository,
    private val medicionesRepo: MedicionesRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            medicionesRepo.ultimas().collect { ultimas ->
                ultimas.forEach { alertasRepo.generarSiCorresponde(it) }
            }
        }
    }

    val lista: StateFlow<List<AlertaUi>> = combine(
        alertasRepo.alertas(),
        alertasRepo.acciones(),
        medicionesRepo.ultimas()
    ) { alertas, acciones, ultimas ->
        alertas.map { a ->
            val cantidad = acciones.count { it.alertaId == a.id }
            val ultima = ultimas.find { it.puntoId == a.puntoId }
            val bloqueo = when {
                !a.abierta -> null
                cantidad == 0 -> "Falta registrar una acción correctiva"
                ultima == null || ultima.estado != EstadoCloro.NORMAL ->
                    "Falta una medición dentro del rango normal"
                else -> null
            }
            AlertaUi(a, cantidad, bloqueo)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val acciones: StateFlow<List<AccionCorrectiva>> = alertasRepo.acciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun registrarAccion(accion: AccionCorrectiva) {
        viewModelScope.launch { alertasRepo.registrarAccion(accion) }
    }

    fun cerrar(alertaId: Int, por: String) {
        val ui = lista.value.find { it.alerta.id == alertaId } ?: return
        if (ui.bloqueo != null) return
        viewModelScope.launch { alertasRepo.cerrar(alertaId, por) }
    }
}

class AlertasViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.obtener(app)
        val alertas = AlertasRepositoryRoom(db.alertaDao(), db.accionDao())
        val mediciones = MedicionesRepositoryRoom(db.medicionDao(), CloroRepository().obtenerPuntos())
        return AlertasViewModel(alertas, mediciones) as T
    }
}