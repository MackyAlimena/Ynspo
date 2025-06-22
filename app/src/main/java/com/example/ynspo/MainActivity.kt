package com.example.ynspo

import android.os.Bundle
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ynspo.ui.navigation.Navigation
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.ThemeViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.ynspo.notification.NOTIFICATION_CHANNEL_ID

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        createNotificationChannel()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkMode = themeViewModel.isDarkMode.value

            YnspoTheme(darkTheme = isDarkMode) {
                Navigation(themeViewModel = themeViewModel)
            }
        }
    }

    private fun createNotificationChannel() {
         val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Learning Android Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)
    }
}