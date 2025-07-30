package com.example.ynspo.ui.screen.boards.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.ynspo.data.model.Board
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.toInspirationItem
import com.example.ynspo.data.model.toUnsplashPhoto
import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.theme.Dimens

@Composable
fun BoardDetailContent(
    board: Board,
    onPhotoClick: (UnsplashPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingL)
    ) {
        // Board title
        Text(
            text = board.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimens.PaddingS)
        )
        
        // Photo count
        Text(
            text = "${board.photos.size} fotos",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Dimens.PaddingL)
        )
        
        // Photos grid
        if (board.photos.isNotEmpty()) {
            PinterestGrid(
                inspirationItems = board.photos,
                onItemClick = { item -> 
                    val photo = item.toUnsplashPhoto()
                    onPhotoClick(photo)
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Este board está vacío",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(Dimens.PaddingS))
                    
                    Text(
                        text = "Agrega algunas fotos a este board desde la pantalla principal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
