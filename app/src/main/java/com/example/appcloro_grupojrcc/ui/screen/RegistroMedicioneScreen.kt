package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun RegistroMedicionScreen(puntoId: Int, onVolver: () -> Unit) {
    PantallaVacia("Registrar medición del punto $puntoId", onVolver)
}