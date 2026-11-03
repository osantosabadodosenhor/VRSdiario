package com.vrsdiario.app

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val prefs = context.getSharedPreferences(
            "versiculos",
            Context.MODE_PRIVATE
        )

        val hoje = SimpleDateFormat(
            "yyyyMMdd",
            Locale.getDefault()
        ).format(Date())

        if (prefs.getString("ultimo_dia", "") == hoje) {
            AlarmScheduler.programar(context)
            return
        }

        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            AlarmScheduler.programar(context)
            return
        }

        if (Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
        ) {
            AlarmScheduler.programar(context)
            return
        }

        val indice = prefs.getInt("indice", 0)
            .coerceIn(0, Versiculos.lista.lastIndex)

        val versiculo = Versiculos.lista[indice]

        val channelId = "versiculo_diario"

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Versículo Diário",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val abrirApp = Intent(
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP

            putExtra("referencia", versiculo.referencia)
            putExtra("texto", versiculo.texto)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            200,
            abrirApp,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            channelId
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Versículo do Dia")
            .setContentText(versiculo.referencia)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(versiculo.texto)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(1, notification)

        prefs.edit()
            .putInt("indice", (indice + 1) % Versiculos.lista.size)
            .putString("ultimo_dia", hoje)
            .putString("ultimo_texto", versiculo.texto)
            .putString("ultima_referencia", versiculo.referencia)
            .apply()

        AlarmScheduler.programar(context)
    }
}
