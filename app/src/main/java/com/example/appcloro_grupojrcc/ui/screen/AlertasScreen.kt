package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.data.model.PuntoMedicion
import com.example.appcloro_grupojrcc.data.model.Rol
import com.example.appcloro_grupojrcc.data.model.TipoDesviacion
import com.example.appcloro_grupojrcc.ui.theme.color
import com.example.appcloro_grupojrcc.ui.viewmodel.AlertaUi
import com.example.appcloro_grupojrcc.ui.viewmodel.AlertasViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel

@Composable
fun AlertasScreen(
    alertasVm: AlertasViewModel,
    cloroVm: CloroViewModel,
    onAccion: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val lista by alertasVm.lista.collectAsStateWithLifecycle()
    val usuario by cloroVm.usuario.collectAsStateWithLifecycle()
    var verAbiertas by remember { mutableStateOf(true) }

    // Cada perfil ve solo las alertas de sus granjas
    val visibles = lista.filter { ui ->
        val granja = cloroVm.puntoPorId(ui.alerta.puntoId)?.granja
        usuario?.granjas?.contains(granja) == true && ui.alerta.abierta == verAbiertas
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Alertas", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pestana("Abiertas", verAbiertas) { verAbiertas = true }
            Pestana("Cerradas", !verAbiertas) { verAbiertas = false }
        }
        if (visibles.isEmpty()) {
            Text(if (verAbiertas) "No hay alertas abiertas" else "No hay alertas cerradas")
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(visibles, key = { it.alerta.id }) { ui ->
                TarjetaAlerta(
                    ui = ui,
                    punto = cloroVm.puntoPorId(ui.alerta.puntoId),
                    rol = usuario?.rol,
                    onAccion = { onAccion(ui.alerta.id) },
                    onCerrar = { alertasVm.cerrar(ui.alerta.id, usuario?.nombre ?: "") }
                )
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
private fun Pestana(texto: String, activa: Boolean, onClick: () -> Unit) {
    if (activa) {
        Button(onClick = onClick) { Text(texto) }
    } else {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White)
        ) { Text(texto) }
    }
}

@Composable
private fun TarjetaAlerta(
    ui: AlertaUi,
    punto: PuntoMedicion?,
    rol: Rol?,
    onAccion: () -> Unit,
    onCerrar: () -> Unit
) {
    val a = ui.alerta
    val color = a.gravedad.color()
    val destacada = a.gravedad == EstadoCloro.CRITICO && a.abierta
    val titulo = if (a.tipo == TipoDesviacion.INSUFICIENTE) "Cloro insuficiente" else "Cloro excesivo"

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = if (destacada) BorderStroke(2.dp, color) else null
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${a.gravedad.name} · $titulo", color = color, style = MaterialTheme.typography.titleMedium)
            Text(
                if (punto != null) "${punto.codigo} · ${punto.granja} - ${punto.nombre}"
                else "Punto ${a.puntoId}"
            )
            Text("Generada: ${a.fecha} (${antiguedad(a.fechaMillis)})")
            Text("Acciones registradas: ${ui.acciones}")
            if (!a.abierta) Text("Cerrada por ${a.cerradaPor ?: "-"}")

            if (a.abierta && (rol == Rol.OPERARIO || rol == Rol.SUPERVISOR)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onAccion) { Text("Registrar acción") }
                    if (rol == Rol.SUPERVISOR) {
                        Button(onClick = onCerrar, enabled = ui.bloqueo == null) { Text("Cerrar") }
                    }
                }
                if (rol == Rol.SUPERVISOR && ui.bloqueo != null) {
                    Text(ui.bloqueo, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun antiguedad(millis: Long): String {
    val min = (System.currentTimeMillis() - millis) / 60_000
    return when {
        min < 60 -> "hace $min min"
        min < 1440 -> "hace ${min / 60} h"
        else -> "hace ${min / 1440} días"
    }
}