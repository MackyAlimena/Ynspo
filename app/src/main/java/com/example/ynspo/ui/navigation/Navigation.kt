package com.example.ynspo.ui.navigation

import BoardsScreen
import HomeScreen
import SharedViewModel
import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.pin.PinDetailScreen
import com.example.ynspo.ui.profile.ProfileScreen
import com.example.ynspo.ui.screen.boards.BoardDetailScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(sharedViewModel: SharedViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(navController, sharedViewModel)
            }
            composable("boards") {
                BoardsScreen(navController)
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("pinDetail") {
                val photo = sharedViewModel.selectedPhoto
                if (photo != null) {
                    PinDetailScreen(photo = photo, navController = navController)
                }
            }
            composable("boardDetail/{boardId}",
                arguments = listOf(navArgument("boardId") { type = NavType.IntType })
            ) { backStackEntry ->
                val boardId = backStackEntry.arguments?.getInt("boardId") ?: 0
                BoardDetailScreen(boardId = boardId, navController = navController)
            }
        }
    }
}
