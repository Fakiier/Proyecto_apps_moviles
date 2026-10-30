package com.example.appcloro_grupojrcc.ui.navigation

object Rutas {
    const val LOGIN = "login"
    const val PUNTOS = "puntos"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"

    const val HISTORIAL = "historial/{id}"
    fun historial(id: Int) = "historial/$id"

    const val REGISTRO_MEDICION = "registro/{id}"
    fun registroMedicion (id: Int) = "registro/$id"

    const val ALERTAS = "alertas"

    const val ACCION = "accion/{alertaId}"
    fun accion(alertaId: Int) = "accion/$alertaId"

    const val PANEL = "panel"

}