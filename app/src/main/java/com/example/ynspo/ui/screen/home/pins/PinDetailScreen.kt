package com.example.ynspo.ui.pin

import androidx.compose.material.icons.Icons
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.offline.Download
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.ui.components.FavoriteDialog

@Composable
fun PinDetailScreen(photo: UnsplashPhoto, boardsViewModel: BoardsViewModel = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(photo.urls.regular),
            contentDescription = photo.description ?: "Photo",
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
//            IconButton(onClick = { showDialog = true }) {
//                Icon(Icons.Default.Favorite, contentDescription = "Favorite", tint = Color.Magenta)
//            }
//            IconButton(onClick = { /* Download logic */ }) {
//                Icon(Icons.Default.Download, contentDescription = "Download", tint = Color.Gray)
//            }
//            IconButton(onClick = { /* Share logic */ }) {
//                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Gray)
//            }
        }
    }

    if (showDialog) {
        FavoriteDialog(photo, boardsViewModel) { showDialog = false }
    }
}
