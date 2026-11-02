package com.example.appcloro_grupojrcc.notificaciones

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.appcloro_grupojrcc.MainActivity
import com.example.appcloro_grupojrcc.data.model.CLORO_NORMAL_MIN
import com.example.appcloro_grupojrcc.data.model.Medicion
import com.example.appcloro_grupojrcc.data.model.PuntoMedicion

object NotificadorCloro {

    private const val CANAL_ID = "alertas_cloro"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val canal = NotificationChannel(CANAL_ID, "Alertas de cloro", NotificationManager.IMPORTANCE_HIGH)
        canal.description = "Avisos de mediciones de cloro fuera del rango crítico"
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(canal)
    }

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission") // se comprueba con tienePermiso() justo antes
    fun notificarCritica(context: Context, punto: PuntoMedicion?, medicion: Medicion) {
        if (!tienePermiso(context)) return

        val tipo = if (medicion.nivelCloro < CLORO_NORMAL_MIN) "insuficiente" else "excesivo"
        val lugar = punto?.let { "${it.granja} - ${it.nombre}" } ?: "Punto ${medicion.puntoId}"

        val abrirApp = PendingIntent.getActivity(
            context,
            medicion.puntoId,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Cloro CRÍTICO · ${punto?.codigo ?: medicion.puntoId}")
            .setContentText("Nivel $tipo: ${"%.2f".format(medicion.nivelCloro)} ppm en $lugar")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(abrirApp)
            .build()

        NotificationManagerCompat.from(context).notify(medicion.puntoId, notificacion)
    }
}