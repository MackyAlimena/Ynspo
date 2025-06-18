package com.example.ynspo.ui.components.dialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.R
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.repository.Board
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun SaveToBoardDialog(
    photo: UnsplashPhoto,
    boardsViewModel: BoardsViewModel,
    onDismiss: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val boards by boardsViewModel.boards.collectAsState()
    val userData by userViewModel.userData.collectAsStateWithLifecycle()
    var showLoginDialog by remember { mutableStateOf(false) }
    
    // Verificar si el usuario está autenticado
    if (userData == null) {
        showLoginDialog = true
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
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.save_to_board))
        },
        text = {
            if (boards.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No boards available")
                }
            } else {
                LazyColumn {
                    items(boards) { board ->                        BoardSelectionItem(
                            board = board,
                            onClick = {
                                boardsViewModel.addToBoard(board.id, photo)
                                // Enviar notificación cuando se guarda un pin (se implementará en el ViewModel)
                                boardsViewModel.sendSavedPinNotification(board.name, photo.description ?: "")
                                onDismiss()
                            }
                        )
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
