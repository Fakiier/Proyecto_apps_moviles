package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.RangoHistorial
import com.example.appcloro_grupojrcc.data.model.resumen
import com.example.appcloro_grupojrcc.ui.theme.color
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.MedicionesViewModel

private fun ppm(valor: Double) = "%.2f".format(valor)

@Composable
fun HistorialScreen(
    puntoId: Int,
    cloroVm: CloroViewModel,
    medicionesVm: MedicionesViewModel,
    onVolver: () -> Unit
) {
    val punto = cloroVm.puntoPorId(puntoId)
    var rango by remember { mutableStateOf(RangoHistorial.DIA) }
    val mediciones by remember(puntoId, rango) { medicionesVm.historial(puntoId, rango) }
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val resumen = remember(mediciones) { mediciones.resumen() }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            if (punto != null) "Historial · ${punto.codigo}" else "Historial del punto $puntoId",
            style = MaterialTheme.typography.headlineSmall
        )
        if (punto != null) Text("${punto.granja} - ${punto.nombre}")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RangoHistorial.entries.forEach { r ->
                FilterChip(
                    selected = rango == r,
                    onClick = { rango = r },
                    label = { Text(r.etiqueta) },
                    colors = FilterChipDefaults.filterChipColors(
                        labelColor = Color.White,
                        selectedContainerColor = Color.White,
                        selectedLabelColor = Color(0xFF0D47A1)
                    )
                )
            }
        }

        if (resumen != null) {
            Text(
                "${resumen.cantidad} mediciones · Prom ${ppm(resumen.promedio)} · " +
                        "Mín ${ppm(resumen.minimo)} · Máx ${ppm(resumen.maximo)} ppm"
            )
        }

        Crossfade(
            targetState = mediciones,
            modifier = Modifier.weight(1f),
            label = "historial"
        ) { lista ->
            if (lista.isEmpty()) {
                Text("Sin mediciones en este período")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(lista, key = { it.id }) { FilaMedicion(it) }
                }
            }
        }

        OutlinedButton(
            onClick = onVolver,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White)
        ) { Text("Volver") }
    }
}

@Composable
private fun FilaMedicion(m: Medicion) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(12.dp).clip(CircleShape).background(m.estado.color()))
        Column {
            Text("${ppm(m.nivelCloro)} ppm · ${m.estado.name}")
            Text("${m.fecha} · ${m.registradoPor}", style = MaterialTheme.typography.bodySmall)
            if (!m.sincronizada) {
                Text(
                    "Pendiente de sincronizar",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFC107)
                )
            }
        }
    }
}