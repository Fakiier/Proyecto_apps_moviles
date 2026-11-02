package com.example.appcloro_grupojrcc.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.appcloro_grupojrcc.data.model.EstadoCloro

fun EstadoCloro.color(): Color = when (this) {
    EstadoCloro.NORMAL -> Color(0xFF4CAF50)
    EstadoCloro.ADVERTENCIA -> Color(0xFFFFC107)
    EstadoCloro.CRITICO -> Color(0xFFF44336)
}