package com.example.ynspo

import android.os.Bundle
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.example.ynspo.ui.navigation.Navigation
import com.example.ynspo.ui.theme.YnspoTheme
import dagger.hilt.android.AndroidEntryPoint
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.ynspo.notification.Notification



@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        createNotificationChannel()

        
        setContent {
            YnspoTheme {
                Navigation()
            }
        }
    }

    private fun createNotificationChannel() {
         val notificationChannel = NotificationChannel(Add commentMore actions
            notificationChannelID,
            "Learning Android Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)
       }
}