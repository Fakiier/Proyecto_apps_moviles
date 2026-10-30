package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.runtime.Composable

@Composable
fun AccionScreen(alertaId: Int, onVolver: () -> Unit) {
    PantallaVacia("Acción correctiva de la alerta $alertaId", onVolver)
}