package com.example.appcloro_grupojrcc.validacion

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ValidadorMedicion {
    const val NIVEL_MAXIMO = 10.0
    private const val FORMATO_FECHA = "dd-MM-yyyy HH:mm"
    private val formatoNumero = Regex("^\\d+([.,]\\d+)?$")
    private val formatoFecha = Regex("^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}$")

    // Devuelve el mensaje de error, o null si el valor es válido
    fun validarNivel(texto: String): String? {
        val t = texto.trim()
        if (t.isEmpty()) return "Ingresa el nivel de cloro"
        if (!formatoNumero.matches(t)) return "Usa solo números, por ejemplo 3,50"
        if ((t.split(',', '.').getOrNull(1)?.length ?: 0) > 2) return "Usa como máximo 2 decimales"
        if (nivelComoDouble(t) > NIVEL_MAXIMO) return "El valor máximo es 10 ppm"
        return null
    }

    fun nivelComoDouble(texto: String): Double = texto.trim().replace(',', '.').toDouble()

    fun validarFecha(texto: String, ahoraMillis: Long = System.currentTimeMillis()): String? {
        val t = texto.trim()
        if (t.isEmpty()) return "Ingresa la fecha y hora"
        if (!formatoFecha.matches(t)) return "Usa el formato dd-MM-aaaa HH:mm"
        val millis = parsearFecha(t) ?: return "La fecha u hora no existe"
        if (millis > ahoraMillis) return "La fecha no puede ser futura"
        return null
    }

    fun fechaActual(): String =
        SimpleDateFormat(FORMATO_FECHA, Locale.getDefault()).format(Date())

    private fun parsearFecha(texto: String): Long? = runCatching {
        SimpleDateFormat(FORMATO_FECHA, Locale.getDefault())
            .apply { isLenient = false }
            .parse(texto)!!.time
    }.getOrNull()
}