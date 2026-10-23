package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme

@Composable
fun LoginScreen(onIngresar: (String, String) -> Boolean) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val camposBlancos = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color(0xFFC5D3E8),
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color(0xFF8FA6C4),
        cursorColor = Color.White
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Control de cloro", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            singleLine = true,
            colors = camposBlancos,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = camposBlancos,
            modifier = Modifier.fillMaxWidth()
        )
        if (error.isNotEmpty()) {
            Text(error, color = Color(0xFFFFB4AB))
        }
        Button(
            onClick = {
                error = when {
                    !correo.contains("@") -> "Ingresa un correo válido"
                    clave.isBlank() -> "Ingresa tu contraseña"
                    !onIngresar(correo, clave) -> "Correo o contraseña incorrectos"
                    else -> ""
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A83D4),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ingresar") }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AppCloro_GrupoJRCCTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            LoginScreen { _, _ -> false }
        }
    }
}