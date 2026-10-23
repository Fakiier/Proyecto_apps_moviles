package com.example.appcloro_grupojrcc.data.model

data class Usuario(
    val correo: String,
    val clave: String,
    val nombre: String,
    val rol: Rol,
    val granjas: List<String>
    )