package com.example.ynspo.ui.screen.boards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ynspo.ui.screen.boards.BoardsViewModel
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.components.dialog.CreateBoardDialog
import com.example.ynspo.security.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardsScreenViewModel @Inject constructor(
    val authManager: AuthManager
) : androidx.lifecycle.ViewModel()

@Composable
fun BoardsScreen(
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel(),
    boardsScreenViewModel: BoardsScreenViewModel = hiltViewModel()
) {
    val boards = boardsViewModel.boards.observeAsState(emptyList())
    var boardsWithPhotos by remember { mutableStateOf<List<com.example.ynspo.data.model.Board>>(emptyList()) }
    
    // Cargar boards con photos cuando cambian los boards
    LaunchedEffect(boards.value) {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            boardsWithPhotos = boardsViewModel.getBoardsWithPhotos()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimens.PaddingL)
        ) {
            // Header con botón de logout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tus Boards",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                // Botón de logout para probar la autenticación
                IconButton(
                    onClick = {
                        boardsScreenViewModel.authManager.clearAuthentication()
                        // Navegar de vuelta a home para forzar la re-autenticación
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.PaddingL))
            
            if (boardsWithPhotos.isEmpty()) {
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
                    items(boardsWithPhotos) { board ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationM),
                            shape = MaterialTheme.shapes.large,
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
                                Text(
                                    board.name, 
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    "${board.photos.size} pins", 
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}