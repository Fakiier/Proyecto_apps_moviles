package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.data.model.ResumenGranja
import com.example.appcloro_grupojrcc.data.model.calcularEstado
import com.example.appcloro_grupojrcc.ui.theme.color
import com.example.appcloro_grupojrcc.ui.viewmodel.PanelJefaturaViewModel
import kotlin.math.roundToInt

@Composable
fun PanelJefaturaScreen(vm: PanelJefaturaViewModel, onVolver: () -> Unit) {
    val ui by vm.ui.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Panel de jefatura", style = MaterialTheme.typography.headlineSmall)
                OutlinedButton(
                    onClick = onVolver,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White)
                ) { Text("Volver") }
            }
        }
        item {
            Text("Alertas abiertas: ${ui.alertasAbiertas} · Pendientes de sincronizar: ${ui.pendientesSync}")
        }
        items(ui.granjas) { TarjetaGranja(it) }
    }
}

@Composable
private fun TarjetaGranja(g: ResumenGranja) {
    val meta = if (g.puntos == 0) 0f else g.normales.toFloat() / g.puntos
    var objetivo by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(meta) { objetivo = meta }   // parte en 0 para que la barra se anime al entrar
    val proporcion by animateFloatAsState(objetivo, tween(700), label = "normales")

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(g.granja, style = MaterialTheme.typography.titleLarge)
            Text(
                "Promedio: ${"%.2f".format(g.promedio)} ppm",
                color = calcularEstado(g.promedio).color()
            )
            Text("${g.normales} normales · ${g.advertencias} en advertencia · ${g.criticos} críticos")
            LinearProgressIndicator(
                progress = { proporcion },
                color = EstadoCloro.NORMAL.color(),
                modifier = Modifier.fillMaxWidth()
            )
            Text("${(proporcion * 100).roundToInt()}% de los puntos en rango normal")
        }
    }
}