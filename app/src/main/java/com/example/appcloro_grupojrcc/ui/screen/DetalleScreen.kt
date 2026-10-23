package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appcloro_grupojrcc.data.model.Rol
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel

@Composable
fun DetalleScreen(viewModel: CloroViewModel, id: Int, onVolver: () -> Unit) {
    val rol by viewModel.rol.collectAsStateWithLifecycle()
    val punto = viewModel.puntoPorId(id)

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (punto == null){
            Text("Punto no encontrado")
        } else {
            Text("${punto.granja} - ${punto.nombre}", style = MaterialTheme.typography.headlineSmall)
            Text("Cloro libre: ${"%.2f".format(punto.nivelCloro)} ppm")
            Text("Código: ${punto.codigo} - Tipo: ${punto.tipo}")
            Text("Estado: ${punto.estado.name}")
            Text("Actualizado: ${punto.actualizado}")
            if (rol == Rol.OPERARIO || rol == Rol.SUPERVISOR) {
                Button(onClick = {/*Fase 3 formulario*/}) { Text("Registrar acción") }
            }
        }
        OutlinedButton(
            onClick = onVolver,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
            border = BorderStroke(1.dp, Color.White)
        ) { Text("Volver") }
    }
}