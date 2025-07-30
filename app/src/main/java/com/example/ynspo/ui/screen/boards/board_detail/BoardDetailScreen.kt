package com.example.ynspo.ui.screen.boards.board_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.data.model.InspirationItem

@Composable
fun BoardDetailScreen(
    boardId: Int,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    sharedViewModel: com.example.ynspo.ui.components.SharedViewModel? = null
) {
    // Estado para el modo de edición
    var isEditMode by remember { mutableStateOf(false) }
    
    // Convert boardId to Long and use observeAsState for LiveData
    val boards = boardsViewModel.boards.observeAsState(initial = emptyList())
    val board = boards.value.find { it.id == boardId.toLong() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingL)
    ) {
        // Header con botón back, título y botón de editar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = Dimens.PaddingL)
        ) {
            // Botón back
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.width(Dimens.PaddingM))
            
            // Título del board
            if (board != null) {
                Text(
                    text = board.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Botón de editar/finalizar
            if (board != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    // Botón temporal para borrar todos los UserPins
                    // Descomenta si necesitas borrar todos los UserPins
                    /*
                    TextButton(
                        onClick = { 
                            boardsViewModel.deleteAllUserPins()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = "Borrar UserPins",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    */
                    
                    // Botón de editar/finalizar
                    IconButton(
                        onClick = { isEditMode = !isEditMode },
                        modifier = Modifier
                            .background(
                                if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.Done else Icons.Default.Edit,
                            contentDescription = if (isEditMode) "Finalizar edición" else "Editar",
                            tint = if (isEditMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Grid de fotos con blur negro
        if (board != null) {
            // Usar el nuevo método que obtiene ambos tipos de items
            val inspirationItems = boardsViewModel.getBoardInspirationItems(board.id.toInt()).observeAsState(emptyList()).value
            PinterestGrid(
                inspirationItems = inspirationItems,
                onItemClick = { inspirationItem ->
                    // Solo permitir navegación si no está en modo edición
                    if (!isEditMode) {
                        // Usar SharedViewModel para pasar el item y navegar a pinDetail
                        sharedViewModel?.selectedPhoto = inspirationItem
                        navController.navigate("pinDetail")
                    }
                },
                onDeleteItem = { inspirationItem ->
                    // Borrar el item del board
                    val itemType = if (inspirationItem.isUserPin) "user_pin" else "unsplash"
                    boardsViewModel.removeInspirationItemFromBoard(board.id, inspirationItem.id, itemType)
                },
                showDeleteButtons = isEditMode
            )
        } else {
            // Estado cuando no se encuentra el board
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Board no encontrado",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}
