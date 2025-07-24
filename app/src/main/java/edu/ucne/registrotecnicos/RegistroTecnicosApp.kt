package edu.ucne.registrotecnicos

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RegistroTecnicosApp : Application()

class NotificationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notification_channel_id",
                "Notificaciones Generales",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Canal para notificaciones generales de la app"

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as
                    NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
