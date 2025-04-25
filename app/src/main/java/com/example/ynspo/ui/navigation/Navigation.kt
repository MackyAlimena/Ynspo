package com.example.ynspo.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.screen.boards.BoardsScreen
import com.example.ynspo.ui.profile.ProfileScreen
import com.example.ynspo.ui.screen.home.HomeScreen
import com.example.ynspo.ui.screen.home.SearchScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }

    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
        ) {
            composable("home") { HomeScreen() }
            composable("boards") { BoardsScreen(paddingValues) }
            composable("profile") { ProfileScreen() }
            composable("search_screen") {SearchScreen()}
        }
    }
}