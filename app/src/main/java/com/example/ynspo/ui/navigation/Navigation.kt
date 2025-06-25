package com.example.ynspo.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ynspo.auth.AuthStatusViewModel
import com.example.ynspo.ui.components.BiometricPromptScreen
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.components.SharedViewModel
import com.example.ynspo.ui.screen.boards.BoardsScreen
import com.example.ynspo.ui.screen.boards.board_detail.BoardDetailScreen
import com.example.ynspo.ui.screen.home.HomeScreen
import com.example.ynspo.ui.screen.pins.PinDetailScreen
import com.example.ynspo.ui.screen.profile.ProfileScreen
import com.example.ynspo.notification.NotificationScreen
import com.example.ynspo.ui.theme.ThemeViewModel
import com.example.ynspo.ui.theme.Dimens

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    sharedViewModel: SharedViewModel = remember { SharedViewModel() },
    authStatusViewModel: AuthStatusViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel
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
                HomeScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
            composable("biometric_auth") {
                BiometricPromptScreen(
                    onAuthenticationSuccess = { navController.navigate("boards_content") },
                    onAuthenticationError = { error -> 
                        // Handle error if needed
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable("boards") {
                // La ruta boards ahora muestra la autenticación biométrica primero
                BiometricPromptScreen(
                    onAuthenticationSuccess = {
                        // Después de autenticación exitosa, navegar al contenido real
                        navController.navigate("boards_content") {
                            // Limpiar la pila para que el usuario no pueda volver a la pantalla de autenticación
                            popUpTo("boards") { inclusive = true }
                        }
                    },
                    onAuthenticationError = { error -> 
                        // Handle error if needed
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable("boards_content") {
                BoardsScreen(navController)
            }
            composable("profile") {
                ProfileScreen(themeViewModel = themeViewModel)
            }
            composable("notifications") {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    NotificationScreen()
                } else {
                    // Fallback para versiones anteriores a Android 13
                    Text(
                        text = "Las notificaciones requieren Android 13 o superior",
                        modifier = Modifier.padding(Dimens.PaddingL)
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
                // Solo muestra los detalles del tablero si viene de la ruta boards_content
                // De lo contrario, redirige a la autenticación biométrica primero
                if (navController.previousBackStackEntry?.destination?.route?.contains("boards_content") == true) {
                    BoardDetailScreen(boardId = boardId, navController = navController, sharedViewModel = sharedViewModel)
                } else {
                    BiometricPromptScreen(
                        onAuthenticationSuccess = {
                            // Después de autenticación exitosa, mostrar el detalle del tablero
                            navController.popBackStack()
                            navController.navigate("boardDetail/$boardId")
                        },
                        onAuthenticationError = { error -> 
                            // Handle error if needed
                        },
                        onCancel = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
