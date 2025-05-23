package com.example.ynspo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.ui.boards.BoardsViewModel

@Composable
fun FavoriteDialog(
    photo: UnsplashPhoto,
    boardsViewModel: BoardsViewModel,
    onDismiss: () -> Unit
) {
    val boards = boardsViewModel.boards.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save to Board") },
        text = {
            Column {
                boards.value.forEach { board ->
                    TextButton(onClick = {
                        boardsViewModel.addToBoard(board.id, photo)
                        onDismiss()
                    }) {
                        Text(board.name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


