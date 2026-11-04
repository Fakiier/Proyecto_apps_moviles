package com.example.appcloro_grupojrcc.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.appcloro_grupojrcc.validacion.ValidarLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val verClave: Boolean = false,
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val errorGeneral: String? = null
)

class LoginViewModel : ViewModel() {

    private val _ui = MutableStateFlow(LoginUiState())
    val ui = _ui.asStateFlow()

    fun onCorreoChange(texto: String) {
        _ui.update {
            it.copy(
                correo = texto,
                errorCorreo = if (texto.isBlank()) null else ValidarLogin.validarCorreo(texto),
                errorGeneral = null
            )
        }
    }

    fun onClaveChange(texto: String) {
        _ui.update {
            it.copy(
                clave = texto,
                errorClave = if (texto.isEmpty()) null else ValidarLogin.validarClave(texto),
                errorGeneral = null
            )
        }
    }

    fun alternarVerClave() {
        _ui.update { it.copy(verClave = !it.verClave) }
    }

    // Valida los campos y, si están bien, consulta las credenciales. Devuelve true si entra.
    fun ingresar(autenticar: (String, String) -> Boolean): Boolean {
        val actual = _ui.value
        val errorCorreo = ValidarLogin.validarCorreo(actual.correo)
        val errorClave = ValidarLogin.validarClave(actual.clave)
        if (errorCorreo != null || errorClave != null) {
            _ui.update { it.copy(errorCorreo = errorCorreo, errorClave = errorClave) }
            return false
        }
        val ok = autenticar(actual.correo.trim(), actual.clave)
        if (!ok) _ui.update { it.copy(errorGeneral = "Correo o contraseña incorrectos") }
        return ok
    }
}