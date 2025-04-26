package com.example.ynspo.ui.pin

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

@Composable
fun PinDetailScreen(
    photoUrl: String,
    boardsViewModel: BoardsViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(photoUrl),
            contentDescription = "Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Favorite, contentDescription = "Favorite")
            }
        }
    }

    if (showDialog) {
        FavoriteDialog(photoUrl = photoUrl, boardsViewModel = boardsViewModel) {
            showDialog = false
        }
    }
}
