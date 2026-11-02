package com.example.appcloro_grupojrcc.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcloro_grupojrcc.data.local.AppDatabase
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.RangoHistorial
import com.example.appcloro_grupojrcc.data.repository.CloroRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepositoryRoom
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicionesViewModel(private val repo: MedicionesRepository) : ViewModel() {

    init {
        viewModelScope.launch { repo.inicializar() }
    }

    val ultimas: StateFlow<List<Medicion>> = repo.ultimas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendientes: StateFlow<Int> = repo.cantidadPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun historial(puntoId: Int, rango: RangoHistorial): Flow<List<Medicion>> =
        repo.historial(puntoId, rango.desdeMillis())

    fun registrar(medicion: Medicion) {
        viewModelScope.launch { repo.registrar(medicion) }
    }
}

class MedicionesViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.obtener(app).medicionDao()
        val repo = MedicionesRepositoryRoom(dao, CloroRepository().obtenerPuntos())
        return MedicionesViewModel(repo) as T
    }
}