package com.example.appcloro_grupojrcc.data.repository

import com.example.appcloro_grupojrcc.data.model.PuntoMedicion
import com.example.appcloro_grupojrcc.data.model.Rol
import com.example.appcloro_grupojrcc.data.model.Usuario

class CloroRepository{
    fun obtenerPuntos(): List<PuntoMedicion> = listOf(
        PuntoMedicion(1, "GN-01", "Granja Norte", "Galpón 1", "Bebedero", 3.80, "22-10-2026 09:00"),
        PuntoMedicion(2, "GN-02", "Granja Norte", "Galpón 2", "Bebedero", 2.60, "22-10-2026 09:00"),
        PuntoMedicion(3, "GN-03", "Granja Norte", "Tanque principal", "Tanque", 4.20, "22-10-2026 09:00"),
        PuntoMedicion(4, "GN-04", "Granja Norte", "Línea de distribución", "Línea", 3.10, "22-10-2026 09:00"),
        PuntoMedicion(5, "GS-01", "Granja Sur", "Galpón 1", "Bebedero", 1.70, "22-10-2026 09:05"),
        PuntoMedicion(6, "GS-02", "Granja Sur", "Galpón 2", "Bebedero", 4.50, "22-10-2026 09:05"),
        PuntoMedicion(7, "GS-03", "Granja Sur", "Tanque principal", "Tanque", 5.60, "22-10-2026 09:05"),
        PuntoMedicion(8, "GS-04", "Granja Sur", "Línea de distribución", "Línea", 3.90, "22-10-2026 09:05"),
        PuntoMedicion(9, "GC-01", "Granja Centro", "Galpón 1", "Bebedero", 6.40, "22-10-2026 09:10"),
        PuntoMedicion(10, "GC-02", "Granja Centro", "Galpón 2", "Bebedero", 3.30, "22-10-2026 09:10"),
        PuntoMedicion(11, "GC-03", "Granja Centro", "Tanque principal", "Tanque", 4.80, "22-10-2026 09:10"),
        PuntoMedicion(12, "GC-04", "Granja Centro", "Línea de distribución", "Línea", 2.90, "22-10-2026 09:10")
    )

    private val usuarios = listOf(
        Usuario("operario@cloro.cl", "1234", Rol.OPERARIO),
        Usuario("supervisor@cloro.cl", "1234", Rol.SUPERVISOR),
        Usuario("jefatura@cloro.cl", "1234", Rol.JEFATURA)
    )

    fun validarUsuario(correo: String, clave: String) =
        usuarios.find { it.correo == correo.trim() && it.clave == clave }

}