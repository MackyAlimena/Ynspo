package com.example.ynspo

import android.os.Bundle
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.example.ynspo.ui.navigation.Navigation
import com.example.ynspo.ui.theme.YnspoTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        
        setContent {
            YnspoTheme {
                Navigation()
            }
        }
    }
}