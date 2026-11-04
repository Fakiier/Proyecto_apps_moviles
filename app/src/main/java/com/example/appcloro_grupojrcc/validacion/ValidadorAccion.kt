package com.example.appcloro_grupojrcc.validacion

object ValidadorAccion {
    const val LARGO_MINIMO = 10
    const val LARGO_MAXIMO = 300

    // Devuelve el mensaje de error, o null si la descripción es válida
    fun validarDescripcion(texto: String): String? {
        val t = texto.trim()
        if (t.isEmpty()) return "Describe la acción realizada"
        if (t.length < LARGO_MINIMO) return "Escribe al menos $LARGO_MINIMO caracteres"
        if (t.length > LARGO_MAXIMO) return "Máximo $LARGO_MAXIMO caracteres"
        return null
    }
}