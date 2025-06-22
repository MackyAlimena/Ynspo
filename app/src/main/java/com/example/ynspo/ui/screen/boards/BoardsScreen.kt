package com.example.ynspo.ui.screen.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.SelectedColor
import com.example.ynspo.ui.components.dialog.CreateBoardDialog

@Composable
fun BoardsScreen(
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel()
) {
    val boards = boardsViewModel.boards.observeAsState(emptyList())
    var showCreateDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(Dimens.PaddingL)
        ) {
            // Header
            Text(
                text = "Tus Boards",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = Dimens.PaddingL)
            )
            
            if (boards.value.isEmpty()) {
                // Estado vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¡Crea tu primer board!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingS))
                        Text(
                            text = "Los boards te ayudan a organizar tus pins favoritos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                // Lista de boards
                LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimens.PaddingL)) {
                    items(boards.value) { board ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SelectedColor.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navController.navigate("boardDetail/${board.id}") }
                        ) {
                            Column(modifier = Modifier.padding(Dimens.PaddingM)) {
                                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)) {
                                    board.photos.take(3).forEach { photo ->
                                        AsyncImage(
                                            model = photo.urls.small,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(Dimens.BaordImageSize)
                                                .clip(MaterialTheme.shapes.medium)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(Dimens.PaddingS))
                                Text(board.name, style = MaterialTheme.typography.titleMedium)
                                Text("${board.photos.size} pins", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
        
        // Floating Action Button
        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(Dimens.PaddingL),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear board"
            )
        }
    }
    
    // Dialog para crear board
    if (showCreateDialog) {
        CreateBoardDialog(
            onDismiss = { showCreateDialog = false },
            onCreateBoard = { name ->
                boardsViewModel.createBoard(name)
            }
        )
    }
}