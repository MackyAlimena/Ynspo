package com.example.ynspo.notification  

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import java.util.Calendar
import com.example.ynspo.data.model.UnsplashPhoto

@HiltViewModel
class ScheduleNotificationViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
): ViewModel() {

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Ynspo Notifications"
            val descriptionText = "Notificaciones de la aplicación Ynspo"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Función sobrecargada que acepta UnsplashPhoto
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(photo: UnsplashPhoto) {
        val title = "💡 Inspiración creativa"
        val message = "Recuerda revisar el pin '${photo.description ?: "sin descripción"}' que guardaste ayer"
        
        scheduleNotification(
            delayInSeconds = 24 * 60 * 60, // 24 horas
            title = title,
            message = message
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(
        delayInSeconds: Long = 3,
        title: String = "Inspiración creativa",
        message: String = "¡No dejes que se te escape la creatividad! Revisa tus tableros en Ynspo."
    ) {
        // Crear un intent para el receptor de notificaciones
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        // Crear un PendingIntent para la transmisión
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Obtener el servicio AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Calcular el tiempo para programar la notificación
        val triggerTime = Calendar.getInstance().apply {
            add(Calendar.SECOND, delayInSeconds.toInt())
        }.timeInMillis

        // Programar la notificación
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
    
    // Método para programar una notificación diaria (por ejemplo, recordatorio)
    @SuppressLint("ScheduleExactAlarm")
    fun scheduleDaily(
        hourOfDay: Int,
        minute: Int,
        title: String = "Inspiración diaria",
        message: String = "¡Es hora de explorar nuevas ideas creativas en Ynspo!"
    ) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1, // Diferente ID para distinguirlo de otras notificaciones
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Configurar la hora para el recordatorio diario
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            
            // Si la hora ya pasó hoy, programar para mañana
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "ynspo_channel"
    }
}