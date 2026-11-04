package com.example.appcloro_grupojrcc.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcloro_grupojrcc.data.local.AppDatabase
import com.example.appcloro_grupojrcc.data.model.PuntoMedicion
import com.example.appcloro_grupojrcc.data.model.ResumenGranja
import com.example.appcloro_grupojrcc.data.model.resumenPorGranja
import com.example.appcloro_grupojrcc.data.repository.AlertasRepository
import com.example.appcloro_grupojrcc.data.repository.AlertasRepositoryRoom
import com.example.appcloro_grupojrcc.data.repository.CloroRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepositoryRoom
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class PanelUi(
    val granjas: List<ResumenGranja> = emptyList(),
    val alertasAbiertas: Int = 0,
    val pendientesSync: Int = 0
)

class PanelJefaturaViewModel(
    private val puntos: List<PuntoMedicion>,
    medicionesRepo: MedicionesRepository,
    alertasRepo: AlertasRepository
) : ViewModel() {
    val ui: StateFlow<PanelUi> = combine(
        medicionesRepo.ultimas(),
        alertasRepo.alertas(),
        medicionesRepo.cantidadPendientes()
    ) { ultimas, alertas, pendientes ->
        PanelUi(
            granjas = resumenPorGranja(puntos, ultimas),
            alertasAbiertas = alertas.count { it.abierta },
            pendientesSync = pendientes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PanelUi())
}

class PanelJefaturaViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.obtener(app)
        val puntos = CloroRepository().obtenerPuntos()
        val mediciones = MedicionesRepositoryRoom(db.medicionDao(), puntos)
        val alertas = AlertasRepositoryRoom(db.alertaDao(), db.accionDao())
        return PanelJefaturaViewModel(puntos, mediciones, alertas) as T
    }
}