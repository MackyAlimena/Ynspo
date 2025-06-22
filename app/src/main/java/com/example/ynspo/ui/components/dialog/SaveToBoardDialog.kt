package com.example.ynspo.ui.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.R
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.Board
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SaveToBoardDialog(
    photo: UnsplashPhoto,
    boardsViewModel: BoardsViewModel,
    onDismiss: () -> Unit
) {
    val boards by boardsViewModel.boards.observeAsState(emptyList())
    var showCreateDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.save_to_board))
        },
        text = {
            Column {
                // Botón para crear nuevo board
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.PaddingM),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    TextButton(
                        onClick = { showCreateDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.padding(end = Dimens.PaddingS)
                        )
                        Text(
                            text = "Crear nuevo board",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Lista de boards existentes
                if (boards.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Crea tu primer board arriba",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    Text(
                        text = "O elige un board existente:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(bottom = Dimens.PaddingS)
                    )
                    
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        items(boards) { board ->
                            BoardSelectionItem(
                                board = board,
                                onClick = {
                                    boardsViewModel.addToBoard(board.id, photo)
                                    boardsViewModel.sendSavedPinNotification(board.name, photo.description ?: "")
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
    
    // Dialog para crear board
    if (showCreateDialog) {
        CreateBoardDialog(
            onDismiss = { showCreateDialog = false },
            onCreateBoard = { name ->
                boardsViewModel.createBoard(name)
                // Opcional: auto-agregar la foto al board recién creado
                // Nota: Esto requerirá una modificación en createBoard para retornar el ID
            }
        )
    }
}

@Composable
fun BoardSelectionItem(
    board: Board,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = board.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingXS)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SaveToBoardDialogPreview() {
    YnspoTheme {
        Surface {
            Column(modifier = Modifier.padding(Dimens.PaddingL)) {
                Text(
                    text = "Save to Board Dialog Preview",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = Dimens.PaddingS)
                )
                
                val sampleBoards = listOf(
                    Board(id = 1, name = "Handmade Decor"),
                    Board(id = 2, name = "Knitting Ideas"),
                    Board(id = 3, name = "Summer Vibes")
                )
                
                sampleBoards.forEach { board ->
                    BoardSelectionItem(
                        board = board,
                        onClick = {}
                    )
                    Divider()
                }
            }
        }
    }
}
