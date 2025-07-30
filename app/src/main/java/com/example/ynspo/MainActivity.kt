package com.example.ynspo

import android.os.Bundle
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ynspo.ui.navigation.Navigation
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.ThemeViewModel
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkMode = themeViewModel.isDarkMode.value

            YnspoTheme(darkTheme = isDarkMode) {
                Navigation(themeViewModel = themeViewModel)
            }
        }
    }


}