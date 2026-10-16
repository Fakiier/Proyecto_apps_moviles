package com.example.appcloro_grupojrcc.ui.screen

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appcloro_grupojrcc.data.model.Rol
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme

@Composable
fun LoginScreen (onRolElegido : (Rol) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Control de cloro", style= MaterialTheme.typography.headlineMedium)
        Text("Selecciona tu perfil")
        Rol.entries.forEach { rol ->
            Button(onClick = {onRolElegido(rol)}, modifier = Modifier.fillMaxWidth()) {
                Text(rol.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    AppCloro_GrupoJRCCTheme { LoginScreen {} }
}