package com.example.ynspo.ui.navigation

import HomeScreen
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ynspo.ui.boards.BoardDetailScreen
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.pin.PinDetailScreen
import com.example.ynspo.ui.screen.boards.BoardsScreen
import com.example.ynspo.ui.screen.profile.ProfileScreen


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
            composable("home") { HomeScreen(navController) }
            composable("boards") { BoardsScreen() }
            composable("profile") { ProfileScreen(paddingValues) }

            composable(
                "pinDetail/{photoUrl}",
                arguments = listOf(navArgument("photoUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val photoUrl = backStackEntry.arguments?.getString("photoUrl") ?: ""
                PinDetailScreen(photoUrl)
            }

            composable("boardDetail/{boardId}") { backStackEntry ->
                val boardId = backStackEntry.arguments?.getString("boardId")?.toInt() ?: 0
                BoardDetailScreen(boardId)
            }

        }
    }
}
