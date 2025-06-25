package com.example.ynspo.ui.screen.boards.board_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.theme.Dimens

@Composable
fun BoardDetailScreen(
    boardId: Int,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    sharedViewModel: com.example.ynspo.ui.components.SharedViewModel? = null
) {
    // Convert boardId to Long and use observeAsState for LiveData
    val boards = boardsViewModel.boards.observeAsState(initial = emptyList())
    val board = boards.value.find { it.id == boardId.toLong() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingL)
    ) {
        // Header con botón back y título
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
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Grid de fotos con blur negro
        if (board != null) {
            PinterestGrid(
                photos = board.photos,
                onPhotoClick = { photo ->
                    // Usar SharedViewModel para pasar la photo y navegar a pinDetail
                    sharedViewModel?.selectedPhoto = photo
                    navController.navigate("pinDetail")
                }
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
