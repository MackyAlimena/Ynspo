package com.example.ynspo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ynspo.ui.screen.home.HomeScreen
import com.example.ynspo.ui.theme.YnspoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YnspoTheme {
                HomeScreen()
            }
        }
    }
}
