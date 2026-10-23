package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.appcloro_grupojrcc.ui.theme.AppCloro_GrupoJRCCTheme

@Composable
fun PuntosScreen(viewModel: CloroViewModel = viewModel(), onPuntoClick: (Int) -> Unit = {}) {    val puntos by viewModel.puntos.collectAsStateWithLifecycle()

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(puntos) { p ->
            val color = when (p.estado) {
                EstadoCloro.NORMAL -> Color(0xFF4CAF50)
                EstadoCloro.ADVERTENCIA -> Color(0xFFFFC107)
                EstadoCloro.CRITICO -> Color(0xFFF44336)
            }
            Card(modifier = Modifier.fillMaxWidth().clickable { onPuntoClick(p.id) }) {
                Column(Modifier.padding(16.dp)) {
                    Text("${p.granja} - ${p.nombre}", style = MaterialTheme.typography.titleMedium)
                    Text("Cloro libre: ${"%.2f".format(p.nivelCloro)} ppm", color = color)
                    Text("Actualizado: ${p.actualizado}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PuntosScreenPreview() {
    AppCloro_GrupoJRCCTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            PuntosScreen()
        }
    }
}