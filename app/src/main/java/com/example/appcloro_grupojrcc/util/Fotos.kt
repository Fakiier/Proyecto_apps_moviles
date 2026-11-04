package com.example.appcloro_grupojrcc.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File

private fun nuevoArchivo(context: Context): File {
    val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
    return File(carpeta, "accion_${System.currentTimeMillis()}.jpg")
}

fun guardarBitmap(context: Context, bitmap: Bitmap): String {
    val archivo = nuevoArchivo(context)
    archivo.outputStream().use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
    return archivo.absolutePath
}

fun copiarDesdeUri(context: Context, uri: Uri): String? = runCatching {
    val archivo = nuevoArchivo(context)
    context.contentResolver.openInputStream(uri)!!.use { entrada ->
        archivo.outputStream().use { salida -> entrada.copyTo(salida) }
    }
    archivo.absolutePath
}.getOrNull()