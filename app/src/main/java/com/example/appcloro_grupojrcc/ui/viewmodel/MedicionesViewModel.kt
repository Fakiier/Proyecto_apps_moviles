package com.example.appcloro_grupojrcc.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.appcloro_grupojrcc.data.local.AppDatabase
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.RangoHistorial
import com.example.appcloro_grupojrcc.data.remote.ClienteApi
import com.example.appcloro_grupojrcc.data.repository.CloroRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepository
import com.example.appcloro_grupojrcc.data.repository.MedicionesRepositoryRoom
import com.example.appcloro_grupojrcc.data.sync.PreferenciasSync
import com.example.appcloro_grupojrcc.data.sync.ResultadoSync
import com.example.appcloro_grupojrcc.data.sync.SincronizadorMediciones
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MedicionesViewModel(
    private val repo: MedicionesRepository,
    private val sincronizador: SincronizadorMediciones,
    prefs: PreferenciasSync
) : ViewModel() {

    init {
        viewModelScope.launch {
            repo.inicializar()
            sincronizador.sincronizar()   // al abrir la app; sin conexión no pasa nada
        }
    }

    val ultimas: StateFlow<List<Medicion>> = repo.ultimas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendientes: StateFlow<Int> = repo.cantidadPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ultimaSync: StateFlow<Long> = prefs.momentoUltimaSync
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val _sincronizando = MutableStateFlow(false)
    val sincronizando: StateFlow<Boolean> = _sincronizando.asStateFlow()

    private val _mensajeSync = MutableStateFlow<String?>(null)
    val mensajeSync: StateFlow<String?> = _mensajeSync.asStateFlow()

    fun historial(puntoId: Int, rango: RangoHistorial): Flow<List<Medicion>> =
        repo.historial(puntoId, rango.desdeMillis())

    fun registrar(medicion: Medicion) {
        viewModelScope.launch { repo.registrar(medicion) }
    }

    fun sincronizar() {
        if (_sincronizando.value) return
        viewModelScope.launch {
            _sincronizando.value = true
            _mensajeSync.value = when (val r = sincronizador.sincronizar()) {
                is ResultadoSync.Exito -> "Sincronizado: ${r.subidas} enviadas, ${r.nuevas} nuevas"
                ResultadoSync.SinConexion -> "Sin conexión con el servidor"
                is ResultadoSync.Error -> r.mensaje
            }
            _sincronizando.value = false
        }
    }

    fun mensajeLeido() {
        _mensajeSync.value = null
    }
}

class MedicionesViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.obtener(app).medicionDao()
        val repo = MedicionesRepositoryRoom(dao, CloroRepository().obtenerPuntos())
        val prefs = PreferenciasSync(app)
        val sincronizador = SincronizadorMediciones(dao, ClienteApi.api, prefs)
        return MedicionesViewModel(repo, sincronizador, prefs) as T
    }
}