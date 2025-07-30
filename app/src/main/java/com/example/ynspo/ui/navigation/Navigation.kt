package com.example.ynspo.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ynspo.auth.AuthStatusViewModel
import com.example.ynspo.security.AuthManager
import com.example.ynspo.ui.components.BiometricPromptScreen
import com.example.ynspo.ui.components.BottomBar
import com.example.ynspo.ui.components.SharedViewModel
import com.example.ynspo.ui.screen.boards.BoardsScreen
import com.example.ynspo.ui.screen.boards.board_detail.BoardDetailScreen
import com.example.ynspo.ui.screen.home.HomeScreen
import com.example.ynspo.ui.screen.pins.PinDetailScreen
import com.example.ynspo.ui.screen.profile.ProfileScreen
import com.example.ynspo.ui.screen.upload.UploadPinScreen
import com.example.ynspo.notification.NotificationScreen
import com.example.ynspo.ui.theme.ThemeViewModel
import com.example.ynspo.ui.theme.Dimens

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val authManager: AuthManager
) : androidx.lifecycle.ViewModel() {
    
    fun isAuthenticated(): Boolean {
        return authManager.isAuthenticated()
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    sharedViewModel: SharedViewModel = remember { SharedViewModel() },
    authStatusViewModel: AuthStatusViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel,
    navigationViewModel: NavigationViewModel = hiltViewModel()
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
                // Verificar si ya está autenticado antes de mostrar la pantalla de autenticación
                if (navigationViewModel.isAuthenticated()) {
                    // Si ya está autenticado, navegar directamente al contenido
                    LaunchedEffect(Unit) {
                        navController.navigate("boards_content") {
                            popUpTo("boards") { inclusive = true }
                        }
                    }
                } else {
                    // Si no está autenticado, mostrar la pantalla de autenticación
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
                val selectedItem = sharedViewModel.selectedPhoto
                if (selectedItem != null) {
                    // Usar directamente InspirationItem
                    PinDetailScreen(inspirationItem = selectedItem, navController = navController)
                }
            }
            composable("uploadPin") {
                UploadPinScreen(navController = navController)
            }
            composable("boardDetail/{boardId}",
                arguments = listOf(navArgument("boardId") { type = NavType.IntType })
            ) { backStackEntry ->
                val boardId = backStackEntry.arguments?.getInt("boardId") ?: 0
                // Verificar si ya está autenticado antes de mostrar el detalle del tablero
                if (navigationViewModel.isAuthenticated()) {
                    // Si ya está autenticado, mostrar directamente el detalle del tablero
                    BoardDetailScreen(boardId = boardId, navController = navController, sharedViewModel = sharedViewModel)
                } else {
                    // Si no está autenticado, mostrar la pantalla de autenticación
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
