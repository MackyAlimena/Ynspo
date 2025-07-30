package com.example.ynspo.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.Board
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.theme.Dimens
import kotlinx.coroutines.delay

@Composable
fun SaveToBoardAfterUploadDialog(
    uploadedPin: InspirationItem,
    boardsViewModel: BoardsViewModel,
    onDismiss: () -> Unit
) {
    val boards by boardsViewModel.boards.observeAsState(emptyList())
    var showCreateDialog by remember { mutableStateOf(false) }
    var savedToBoardId by remember { mutableStateOf<Long?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    // Log para debuggear los boards
    LaunchedEffect(boards) {
        android.util.Log.d("SaveToBoardDialog", "📋 Boards actualizados en diálogo: ${boards.size} boards")
        boards.forEach { board ->
            android.util.Log.d("SaveToBoardDialog", "📋 Board '${board.name}' tiene ${board.photos.size} pins")
        }
    }

    // Auto-dismiss después de un guardado exitoso
    LaunchedEffect(savedToBoardId) {
        savedToBoardId?.let {
            android.util.Log.d("SaveToBoardDialog", "✅ Pin guardado en board $it, cerrando en 1.5s...")
            delay(1500) // Mostrar el check por 1.5 segundos
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
            ) {
                if (savedToBoardId != null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = if (savedToBoardId != null) "¡Guardado!" else "¡Pin subido exitosamente!"
                )
            }
        },
        text = {
            if (savedToBoardId != null) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Pin guardado exitosamente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            } else {
                Column {
                    Text(
                        text = "¿Te gustaría guardar este pin en un board?",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = Dimens.PaddingM)
                    )
                    
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
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isProcessing
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
                            modifier = Modifier.heightIn(max = Dimens.DialogMaxHeight)
                        ) {
                            items(boards) { board ->
                                BoardSelectionItem(
                                    board = board,
                                    isProcessing = isProcessing,
                                    onClick = {
                                        android.util.Log.d("SaveToBoardDialog", "🔄 Guardando pin en board '${board.name}' (ID: ${board.id})")
                                        isProcessing = true
                                        boardsViewModel.addInspirationItemToBoard(board.id, uploadedPin)
                                        boardsViewModel.sendSavedPinNotification(board.name, uploadedPin.description ?: "")
                                        savedToBoardId = board.id
                                        android.util.Log.d("SaveToBoardDialog", "✅ Pin guardado, savedToBoardId: $savedToBoardId")
                                        isProcessing = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (savedToBoardId == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        enabled = !isProcessing
                    ) {
                        Text("No, gracias")
                    }
                }
            }
        }
    )
    
    // Dialog para crear board
    if (showCreateDialog) {
        CreateBoardDialog(
            onDismiss = { showCreateDialog = false },
            onCreateBoard = { name ->
                boardsViewModel.createBoard(name)
                showCreateDialog = false
            }
        )
    }
} 