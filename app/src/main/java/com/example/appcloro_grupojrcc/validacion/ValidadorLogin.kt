package com.example.appcloro_grupojrcc.validacion

object ValidadorLogin {
    private val formatoCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    const val LARGO_MINIMO_CLAVE = 4

    fun validarCorreo(texto: String): String? {
        val t = texto.trim()
        if (t.isEmpty()) return  "Ingresa tu correo"
        if (!formatoCorreo.matches(t)) return "Usa un correo válido, ej: nombre@cloro.cl"
        return null
    }

    fun validarClave(texto: String): String? {
        if (texto.isEmpty()) return "Ingresa tu contraseña"
        if (texto.length < LARGO_MINIMO_CLAVE) return "La contraseña debe tener al menos $LARGO_MINIMO_CLAVE caracteres"
        return null
    }
}