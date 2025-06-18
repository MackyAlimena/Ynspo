package com.example.ynspo.ui.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthenticatedSaveToBoardDialog(
    photo: UnsplashPhoto,
    boardsViewModel: BoardsViewModel,
    onDismiss: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val userData by userViewModel.userData.collectAsStateWithLifecycle()
    var showLoginDialog by remember { mutableStateOf(false) }
    
    // Verificar si el usuario está autenticado
    LaunchedEffect(Unit) {
        if (userData == null) {
            showLoginDialog = true
        }
    }
    
    // Mostrar diálogo de login si es necesario
    if (showLoginDialog && Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        LoginRequiredDialog(
            onDismiss = { 
                showLoginDialog = false
                onDismiss()
            },
            onLogin = { userViewModel.launchCredentialManager() }
        )
    } else {
        // Mostrar el diálogo de guardar en tablero si está autenticado
        SaveToBoardDialog(
            photo = photo,
            boardsViewModel = boardsViewModel,
            onDismiss = onDismiss
        )
    }
}
