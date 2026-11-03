package com.vrsdiario.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        when (intent?.action) {

            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED -> {

                val agora = Calendar.getInstance()

                val hora = agora.get(Calendar.HOUR_OF_DAY)

                if (hora >= 7) {

                    val notificationIntent = Intent(
                        context,
                        NotificationReceiver::class.java
                    )

                    context.sendBroadcast(notificationIntent)

                } else {

                    AlarmScheduler.programar(context)
                }
            }
        }
    }
}
