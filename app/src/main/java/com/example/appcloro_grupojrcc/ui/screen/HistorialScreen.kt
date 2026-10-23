package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun HistorialScreen(puntoId: Int, onVolver: () -> Unit) {
    PantallaVacia("Historial del punto $puntoId", onVolver)
}