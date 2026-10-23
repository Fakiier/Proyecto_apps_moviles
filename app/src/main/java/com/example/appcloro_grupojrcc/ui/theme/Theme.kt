package com.example.appcloro_grupojrcc.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresApp = lightColorScheme(
    primary = Color(0xFF6FA3E0),
    onPrimary = Color(0xFF0D2240),
    background = Color(0xFF1F3A5F),
    onBackground = Color.White,
    surface = Color.White,
    onSurface = Color(0xFF1B1B1F),
    surfaceContainerHighest = Color(0xFFEAF0F8)
)

@Composable
fun AppCloro_GrupoJRCCTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresApp, typography = Typography, content = content)
}