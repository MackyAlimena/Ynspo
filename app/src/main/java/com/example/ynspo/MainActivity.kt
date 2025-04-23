package com.example.ynspo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.example.ynspo.ui.navigation.Navigation
import com.example.ynspo.ui.theme.YnspoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YnspoTheme {
                Navigation()
            }
        }
    }
}