package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme

@Composable
fun LoginScreen(onIngresar: (String, String) -> Boolean) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var verClave by remember { mutableStateOf(false) }

    val camposBlancos = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color(0xFFC5D3E8),
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color(0xFF8FA6C4),
        cursorColor = Color.White,
        focusedLeadingIconColor = Color.White,
        unfocusedLeadingIconColor = Color(0xFFC5D3E8),
        focusedTrailingIconColor = Color.White,
        unfocusedTrailingIconColor = Color(0xFFC5D3E8)
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
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = camposBlancos,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { verClave = !verClave }) {
                    Icon(
                        if (verClave) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (verClave) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (verClave) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = camposBlancos,
            modifier = Modifier.fillMaxWidth()
        )
        if (error.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFB4AB))
                Text(error, color = Color(0xFFFFB4AB))
            }
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