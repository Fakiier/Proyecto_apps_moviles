package com.example.appcloro_grupojrcc.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.appcloro_grupojrcc.data.model.AccionCorrectiva
import com.example.appcloro_grupojrcc.data.model.TipoDesviacion
import com.example.appcloro_grupojrcc.ui.theme.color
import com.example.appcloro_grupojrcc.ui.viewmodel.AlertasViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel
import com.example.appcloro_grupojrcc.util.copiarDesdeUri
import com.example.appcloro_grupojrcc.util.guardarBitmap
import com.example.appcloro_grupojrcc.validacion.ValidadorMedicion
import java.io.File

@Composable
fun AccionScreen(
    alertaId: Int,
    cloroVm: CloroViewModel,
    alertasVm: AlertasViewModel,
    onVolver: () -> Unit
) {
    val context = LocalContext.current
    val lista by alertasVm.lista.collectAsStateWithLifecycle()
    val acciones by alertasVm.acciones.collectAsStateWithLifecycle()
    val usuario by cloroVm.usuario.collectAsStateWithLifecycle()

    val alerta = lista.find { it.alerta.id == alertaId }?.alerta
    val punto = alerta?.let { cloroVm.puntoPorId(it.puntoId) }
    val deLaAlerta = acciones.filter { it.alertaId == alertaId }

    var descripcion by rememberSaveable { mutableStateOf("") }
    var foto by rememberSaveable { mutableStateOf<String?>(null) }
    var error by rememberSaveable { mutableStateOf<String?>(null) }
    var guardada by rememberSaveable { mutableStateOf(false) }

    val tomarFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            foto = guardarBitmap(context, bitmap)
            guardada = false
        }
    }
    val elegirFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            foto = copiarDesdeUri(context, uri)
            guardada = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Acción correctiva", style = MaterialTheme.typography.headlineSmall)

        if (alerta == null) {
            Text("Alerta no encontrada")
        } else {
            val titulo =
                if (alerta.tipo == TipoDesviacion.INSUFICIENTE) "Cloro insuficiente" else "Cloro excesivo"
            Text("${alerta.gravedad.name} · $titulo", color = alerta.gravedad.color())
            Text(
                if (punto != null) "${punto.codigo} · ${punto.granja} - ${punto.nombre}"
                else "Punto ${alerta.puntoId}"
            )
            Text("Responsable: ${usuario?.nombre ?: "-"}")

            OutlinedTextField(
                value = descripcion,
                onValueChange = {
                    descripcion = it
                    error = null
                    guardada = false
                },
                label = { Text("Descripción de la acción") },
                isError = error != null,
                supportingText = {
                    AnimatedVisibility(visible = error != null) { Text(error ?: "") }
                },
                minLines = 3,
                colors = coloresCampo(),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Foto (opcional)")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { tomarFoto.launch(null) }) { Text("Tomar foto") }
                OutlinedButton(
                    onClick = { elegirFoto.launch("image/*") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = BorderStroke(1.dp, Color.White)
                ) { Text("Galería") }
            }
            foto?.let { ruta ->
                AsyncImage(
                    model = File(ruta),
                    contentDescription = "Foto de la acción",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
                TextButton(onClick = { foto = null }) { Text("Quitar foto", color = Color.White) }
            }

            Button(
                onClick = {
                    val texto = descripcion.trim()
                    error = when {
                        texto.isEmpty() -> "Describe la acción realizada"
                        texto.length < 10 -> "Escribe al menos 10 caracteres"
                        texto.length > 300 -> "Máximo 300 caracteres"
                        else -> null
                    }
                    if (error == null) {
                        alertasVm.registrarAccion(
                            AccionCorrectiva(
                                id = 0,
                                alertaId = alerta.id,
                                puntoId = alerta.puntoId,
                                medicionId = alerta.medicionId,
                                descripcion = texto,
                                responsable = usuario?.nombre ?: "Desconocido",
                                fecha = ValidadorMedicion.fechaActual(),
                                fotoUri = foto
                            )
                        )
                        descripcion = ""
                        foto = null
                        guardada = true
                    }
                }
            ) { Text("Guardar acción") }

            AnimatedVisibility(visible = guardada) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                    Text("Acción registrada")
                }
            }

            if (deLaAlerta.isNotEmpty()) {
                Text("Acciones registradas", style = MaterialTheme.typography.titleMedium)
                deLaAlerta.forEach { a ->
                    Card(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(a.descripcion)
                            Text(
                                "${a.responsable} · ${a.fecha} · ${a.estado.name}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            a.fotoUri?.let { ruta ->
                                AsyncImage(
                                    model = File(ruta),
                                    contentDescription = "Foto de la acción",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxWidth().height(120.dp)
                                )
                            }
                        }
                    }
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
private fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    errorTextColor = Color.White,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = Color(0xFFC5D3E8),
    errorLabelColor = Color(0xFFFFB4AB),
    focusedBorderColor = Color.White,
    unfocusedBorderColor = Color(0xFF8FA6C4),
    errorBorderColor = Color(0xFFFFB4AB),
    cursorColor = Color.White,
    errorCursorColor = Color(0xFFFFB4AB),
    errorSupportingTextColor = Color(0xFFFFB4AB)
)