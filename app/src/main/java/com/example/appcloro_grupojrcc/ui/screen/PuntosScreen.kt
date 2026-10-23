package com.example.appcloro_grupojrcc.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel

@Composable
fun PuntosScreen(
    viewModel: CloroViewModel = viewModel(),
    onPuntoClick: (Int) -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val puntos by viewModel.puntos.collectAsStateWithLifecycle()
    val usuario by viewModel.usuario.collectAsStateWithLifecycle()

    val visibles = puntos.filter { p -> usuario?.granjas?.contains(p.granja) == true }
    val porGranja = visibles.groupBy { it.granja }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(usuario?.nombre ?: "", style = MaterialTheme.typography.titleMedium)
                    Text(usuario?.Rol?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "")
                }
                OutlinedButton(
                    onClick = onCerrarSesion,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White)
                ) { Text("Salir") }
            }
        }
        porGranja.forEach { (granja, lista) ->
            item {
                Text(
                    granja,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            items(lista) { p ->
                val color = when (p.estado) {
                    EstadoCloro.NORMAL -> Color(0xFF4CAF50)
                    EstadoCloro.ADVERTENCIA -> Color(0xFFFFC107)
                    EstadoCloro.CRITICO -> Color(0xFFF44336)
                }
                Card(modifier = Modifier.fillMaxWidth().clickable { onPuntoClick(p.id) }) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${p.nombre} - ${p.tipo}", style = MaterialTheme.typography.titleMedium)
                        Text("Cloro libre: ${"%.2f".format(p.nivelCloro)} ppm", color = color)
                        Text("Actualizado: ${p.actualizado}")
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PuntosScreenPreview() {
    val vm = CloroViewModel().apply { iniciarSesion("jefatura@cloro.cl", "1234") }
    AppCloro_GrupoJRCCTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PuntosScreen(vm)
        }
    }
}