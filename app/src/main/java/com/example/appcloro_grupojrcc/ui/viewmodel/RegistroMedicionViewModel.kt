package com.example.appcloro_grupojrcc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.calcularEstado
import com.example.appcloro_grupojrcc.validacion.ValidadorMedicion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ErroresRegistro(val nivel: String? = null, val fecha: String? = null) {
    val hayErrores get() = nivel != null || fecha != null
}

data class RegistroUiState(
    val nivel: String = "",
    val fecha: String = ValidadorMedicion.fechaActual(),
    val errores: ErroresRegistro = ErroresRegistro(),
    val estadoPrevio: EstadoCloro? = null,
    val guardada: Boolean = false
)

class RegistroMedicionViewModel : ViewModel() {

    private val _ui = MutableStateFlow(RegistroUiState())
    val ui = _ui.asStateFlow()

    fun onNivelChange(texto: String) {
        val error = if (texto.isBlank()) null else ValidadorMedicion.validarNivel(texto)
        _ui.update {
            it.copy(
                nivel = texto,
                errores = it.errores.copy(nivel = error),
                estadoPrevio = if (texto.isNotBlank() && error == null)
                    calcularEstado(ValidadorMedicion.nivelComoDouble(texto)) else null,
                guardada = false
            )
        }
    }

    fun onFechaChange(texto: String) {
        val error = if (texto.isBlank()) null else ValidadorMedicion.validarFecha(texto)
        _ui.update { it.copy(fecha = texto, errores = it.errores.copy(fecha = error), guardada = false) }
    }

    // Devuelve la medición lista para guardar, o null si hay errores
    fun validarYCrear(puntoId: Int, registradoPor: String): Medicion? {
        val actual = _ui.value
        val errores = ErroresRegistro(
            nivel = ValidadorMedicion.validarNivel(actual.nivel),
            fecha = ValidadorMedicion.validarFecha(actual.fecha)
        )
        if (errores.hayErrores) {
            _ui.update { it.copy(errores = errores) }
            return null
        }
        val medicion = Medicion(
            id = 0,
            puntoId = puntoId,
            nivelCloro = ValidadorMedicion.nivelComoDouble(actual.nivel),
            fecha = actual.fecha.trim(),
            registradoPor = registradoPor,
            sincronizada = false // queda pendiente hasta que exista la sincronización
        )
        _ui.value = RegistroUiState(guardada = true)
        return medicion
    }
}