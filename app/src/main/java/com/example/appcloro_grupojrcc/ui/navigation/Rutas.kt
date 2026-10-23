package com.example.appcloro_grupojrcc.ui.navigation

object Rutas {
    const val LOGIN = "login"
    const val PUNTOS = "puntos"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"
}