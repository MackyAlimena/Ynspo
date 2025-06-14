package com.example.ynspo.ui.navigation

import BoardsScreen
import HomeScreen
import SharedViewModel
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ynspo.notification.NotificationScreen
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.profile.ProfileScreen
import com.example.ynspo.ui.screen.boards.BoardDetailScreen
import com.example.ynspo.ui.screen.pins.PinDetailScreen

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(sharedViewModel: SharedViewModel = remember { SharedViewModel() }) {
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
            }            composable("boards") {
                BoardsScreen(navController)
            }
            composable("profile") {
                ProfileScreen()
            }
            composable("notifications") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    NotificationScreen()
                } else {
                    // Fallback para versiones anteriores a Android 13
                    Text(
                        text = "Las notificaciones requieren Android 13 o superior",
                        modifier = Modifier.padding(16.dp)
                    )
                }
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
