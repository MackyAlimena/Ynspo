package com.example.ynspo.ui.screen.pins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.ui.components.dialog.AuthenticatedSaveToBoardDialog
import com.example.ynspo.ui.theme.BackgroundColor

@Composable
fun PinDetailScreen(
    photo: UnsplashPhoto,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = { navController.popBackStack() }
) {
    var showSaveToBoardDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Display the pin image
        PinImage(photo = photo)
        
        // Display pin actions (favorite, share)
        PinActions(
            onFavoriteClick = { showSaveToBoardDialog = true },
            onShareClick = { /* Share functionality */ }
        )
        
        // Display pin description
        PinDescription(photo = photo)
    }
      // Save to board dialog
    if (showSaveToBoardDialog) {
        AuthenticatedSaveToBoardDialog(
            photo = photo,
            boardsViewModel = boardsViewModel,
            onDismiss = { showSaveToBoardDialog = false }
        )
    }
}
