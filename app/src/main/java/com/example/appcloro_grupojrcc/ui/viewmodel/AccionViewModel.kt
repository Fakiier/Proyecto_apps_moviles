package com.example.appcloro_grupojrcc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva
import com.example.appcloro_grupojrcc.data.model.Alerta
import com.example.appcloro_grupojrcc.validacion.ValidadorAccion
import com.example.appcloro_grupojrcc.validacion.ValidadorMedicion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AccionUiState(
    val descripcion: String = "",
    val foto: String? = null,
    val error: String? = null,
    val guardada: Boolean = false
)

class AccionViewModel : ViewModel() {

    private val _ui = MutableStateFlow(AccionUiState())
    val ui = _ui.asStateFlow()

    fun onDescripcionChange(texto: String) {
        _ui.update { it.copy(descripcion = texto, error = null, guardada = false) }
    }

    fun onFotoChange(ruta: String?) {
        _ui.update { it.copy(foto = ruta, guardada = false) }
    }

    // Devuelve la acción lista para guardar, o null si la descripción no es válida
    fun validarYCrear(alerta: Alerta, responsable: String): AccionCorrectiva? {
        val actual = _ui.value
        val error = ValidadorAccion.validarDescripcion(actual.descripcion)
        if (error != null) {
            _ui.update { it.copy(error = error) }
            return null
        }
        val accion = AccionCorrectiva(
            id = 0,
            alertaId = alerta.id,
            puntoId = alerta.puntoId,
            medicionId = alerta.medicionId,
            descripcion = actual.descripcion.trim(),
            responsable = responsable,
            fecha = ValidadorMedicion.fechaActual(),
            fotoUri = actual.foto
        )
        _ui.value = AccionUiState(guardada = true)
        return accion
    }
}