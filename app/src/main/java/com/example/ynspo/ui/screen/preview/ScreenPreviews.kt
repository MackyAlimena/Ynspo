package com.example.ynspo.ui.screen.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls

import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens

@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
fun HomeScreenPreview() {
    YnspoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                Text(
                    text = "Ynspo - Inspiración Creativa",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
                
                PinterestGrid(
                    photos = samplePhotos,
                    onPhotoClick = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Empty State Preview")
@Composable
fun EmptyStatePreview() {
    YnspoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No hay contenido disponible",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(Dimens.PaddingS))
                    
                    Text(
                        text = "Intenta buscar algo diferente",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Sample data for previews
private val samplePhotos = listOf(
    UnsplashPhoto(
        id = "1",
        description = "Beautiful landscape",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
            regular = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
            full = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5"
        )
    ),
    UnsplashPhoto(
        id = "2",
        description = "Creative artwork",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
            regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
            full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
        )
    ),
    UnsplashPhoto(
        id = "3",
        description = "Inspirational design",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1542435503-956c469947f6",
            regular = "https://images.unsplash.com/photo-1542435503-956c469947f6",
            full = "https://images.unsplash.com/photo-1542435503-956c469947f6"
        )
    )
)

@Preview(showSystemUi = true)
@Composable
fun LoadingStatePreview() {
    YnspoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HomeHeaderPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingL),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Explore",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
