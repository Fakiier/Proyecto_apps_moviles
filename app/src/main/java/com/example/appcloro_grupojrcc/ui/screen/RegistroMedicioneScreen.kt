package com.example.appcloro_grupojrcc.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcloro_grupojrcc.data.model.CLORO_NORMAL_MAX
import com.example.appcloro_grupojrcc.data.model.CLORO_NORMAL_MIN
import com.example.appcloro_grupojrcc.data.model.EstadoCloro
import com.example.appcloro_grupojrcc.ui.theme.color
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.MedicionesViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.RegistroMedicionViewModel

@Composable
fun RegistroMedicionScreen(
    puntoId: Int,
    cloroVm: CloroViewModel,
    medicionesVm: MedicionesViewModel,
    onVolver: () -> Unit,
    formVm: RegistroMedicionViewModel = viewModel()
) {
    val ui by formVm.ui.collectAsStateWithLifecycle()
    val usuario by cloroVm.usuario.collectAsStateWithLifecycle()
    val punto = cloroVm.puntoPorId(puntoId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registrar medición", style = MaterialTheme.typography.headlineSmall)
        if (punto != null) Text("${punto.codigo} · ${punto.granja} - ${punto.nombre}")
        Text(
            "Rango normal: ${"%.2f".format(CLORO_NORMAL_MIN)} a ${"%.2f".format(CLORO_NORMAL_MAX)} ppm",
            style = MaterialTheme.typography.bodySmall
        )

        CampoValidado(
            valor = ui.nivel,
            onCambio = formVm::onNivelChange,
            etiqueta = "Cloro libre (ppm)",
            icono = Icons.Default.Create,
            error = ui.errores.nivel,
            teclado = KeyboardType.Decimal
        )
        CampoValidado(
            valor = ui.fecha,
            onCambio = formVm::onFechaChange,
            etiqueta = "Fecha y hora (dd-MM-aaaa HH:mm)",
            icono = Icons.Default.DateRange,
            error = ui.errores.fecha
        )

        EstadoPrevio(ui.estadoPrevio)

        Button(
            onClick = {
                val medicion = formVm.validarYCrear(puntoId, usuario?.nombre ?: "Desconocido")
                if (medicion != null) medicionesVm.registrar(medicion)
            }
        ) { Text("Guardar medición") }

        AnimatedVisibility(visible = ui.guardada) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                Text("Medición guardada (pendiente de sincronizar)")
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
private fun CampoValidado(
    valor: String,
    onCambio: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    error: String?,
    teclado: KeyboardType = KeyboardType.Text
) {
    val rojo = Color(0xFFFFB4AB)
    val gris = Color(0xFFC5D3E8)
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = {
            if (error != null) Icon(Icons.Default.Warning, contentDescription = "Error")
        },
        isError = error != null,
        supportingText = {
            AnimatedVisibility(visible = error != null) { Text(error ?: "") }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            errorTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = gris,
            errorLabelColor = rojo,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color(0xFF8FA6C4),
            errorBorderColor = rojo,
            cursorColor = Color.White,
            errorCursorColor = rojo,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = gris,
            errorLeadingIconColor = rojo,
            errorTrailingIconColor = rojo,
            errorSupportingTextColor = rojo
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun EstadoPrevio(estado: EstadoCloro?) {
    Crossfade(targetState = estado, label = "estado") { e ->
        if (e == null) {
            Spacer(Modifier.height(24.dp))
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(Modifier.size(12.dp).clip(CircleShape).background(e.color()))
                Text("Estado de esta medición: ${e.name}")
            }
        }
    }
}