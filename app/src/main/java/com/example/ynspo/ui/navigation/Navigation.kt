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
import com.example.ynspo.auth.AuthStatusViewModel
import com.example.ynspo.ui.components.AuthenticationCheck
import com.example.ynspo.ui.components.BiometricPromptScreen
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.screen.pins.PinDetailScreen
import com.example.ynspo.ui.profile.ProfileScreen
import com.example.ynspo.ui.screen.boards.BoardDetailScreen
import androidx.compose.runtime.remember
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    sharedViewModel: SharedViewModel = remember { SharedViewModel() },
    authStatusViewModel: AuthStatusViewModel = hiltViewModel()
) {
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
            }            composable("biometric_auth") {
                BiometricPromptScreen(onSuccess = { navController.navigate("boards_content") })
            }            composable("boards") {
                // La ruta boards ahora muestra la autenticación biométrica primero
                BiometricPromptScreen(
                    onSuccess = { 
                        // Después de autenticación exitosa, navegar al contenido real
                        navController.navigate("boards_content") {
                            // Limpiar la pila para que el usuario no pueda volver a la pantalla de autenticación
                            popUpTo("boards") { inclusive = true }
                        }
                    }
                )
            }
            composable("boards_content") {
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
            }            composable("boardDetail/{boardId}",
                arguments = listOf(navArgument("boardId") { type = NavType.IntType })
            ) { backStackEntry ->
                val boardId = backStackEntry.arguments?.getInt("boardId") ?: 0
                // Solo muestra los detalles del tablero si viene de la ruta boards_content
                // De lo contrario, redirige a la autenticación biométrica primero
                if (navController.previousBackStackEntry?.destination?.route?.contains("boards_content") == true) {
                    BoardDetailScreen(boardId = boardId, navController = navController)
                } else {
                    BiometricPromptScreen(
                        onSuccess = {
                            // Después de autenticación exitosa, mostrar el detalle del tablero
                            navController.popBackStack()
                            navController.navigate("boardDetail/$boardId")
                        }
                    )
                }
            }
        }
    }
}
