package com.example.ynspo.ui.pin

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.components.FavoriteDialog
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.boards.BoardsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.data.model.UnsplashPhoto

@Composable
fun PinDetailScreen(
    photo: UnsplashPhoto,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.urls.regular),
            contentDescription = "Photo",
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        IconButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 400.dp)
                .size(56.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        if (showDialog) {
            FavoriteDialog(photo = photo, boardsViewModel = boardsViewModel) {
                showDialog = false
            }
        }
    }
}

