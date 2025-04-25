package com.example.ynspo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.ui.boards.BoardsViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun FavoriteDialog(photo: UnsplashPhoto, viewModel: BoardsViewModel, onDismiss: () -> Unit) {
    val boards by viewModel.boards.collectAsState()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Save to board") },
        text = {
            Column {
                boards.forEach { board ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.addToBoard(board.id, photo)
                                onDismiss()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(board.name)
                    }
                }
            }
        },
        confirmButton = {}
    )
}
