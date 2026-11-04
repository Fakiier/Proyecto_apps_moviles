package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme
import com.example.appcloro_grupojrcc.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onIngresar: (String, String) -> Boolean
) {
    val ui by vm.ui.collectAsStateWithLifecycle()

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
        unfocusedTrailingIconColor = Color(0xFFC5D3E8),
        errorTextColor = Color.White,
        errorLabelColor = Color(0xFFFFB4AB),
        errorBorderColor = Color(0xFFFFB4AB),
        errorLeadingIconColor = Color(0xFFFFB4AB),
        errorTrailingIconColor = Color(0xFFFFB4AB),
        errorCursorColor = Color(0xFFFFB4AB),
        errorSupportingTextColor = Color(0xFFFFB4AB)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Control de cloro", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = ui.correo,
            onValueChange = vm::onCorreoChange,
            label = { Text("Correo") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            isError = ui.errorCorreo != null,
            supportingText = { ui.errorCorreo?.let { MensajeError(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = camposBlancos,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ui.clave,
            onValueChange = vm::onClaveChange,
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { vm.alternarVerClave() }) {
                    Icon(
                        if (ui.verClave) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (ui.verClave) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            isError = ui.errorClave != null,
            supportingText = { ui.errorClave?.let { MensajeError(it) } },
            singleLine = true,
            visualTransformation = if (ui.verClave) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = camposBlancos,
            modifier = Modifier.fillMaxWidth()
        )

        ui.errorGeneral?.let { MensajeError(it) }

        Button(
            onClick = { vm.ingresar(onIngresar) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A83D4),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ingresar") }
    }
}

@Composable
private fun MensajeError(texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFB4AB))
        Text(texto, color = Color(0xFFFFB4AB))
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