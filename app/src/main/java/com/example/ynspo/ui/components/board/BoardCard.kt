package com.example.ynspo.ui.components.board

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.Board
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.YnspoTheme

@Composable
fun BoardCard(
    board: Board,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.ElevationS
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusM)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingL),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Board preview (first photo or placeholder)
            if (board.photos.isNotEmpty()) {
                AsyncImage(
                    model = board.photos.first().urls.small,
                    contentDescription = "Board preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(Dimens.BaordImageSize)
                        .clip(RoundedCornerShape(Dimens.CornerRadiusS))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(Dimens.BaordImageSize)
                        .clip(RoundedCornerShape(Dimens.CornerRadiusS)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📋",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(Dimens.PaddingL))
            
            // Board info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = board.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingXS))
                
                Text(
                    text = "${board.photos.size} fotos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun BoardThumbnail(
    photo: UnsplashPhoto,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = photo.urls.small,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier            .size(Dimens.BaordImageSize)
            .clip(RoundedCornerShape(Dimens.PaddingS))
    )
}

@Preview(showBackground = true)
@Composable
fun BoardCardPreview() {
    YnspoTheme {
        val samplePhotos = listOf(
            UnsplashPhoto(
                id = "1",
                description = "Sample Photo 1",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1523567830207-96731740fa71?w=200",
                    regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71?w=400",
                    full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
                )
            ),
            UnsplashPhoto(
                id = "2",
                description = "Sample Photo 2",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611?w=200",
                    regular = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611?w=400",
                    full = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611"
                )
            )
        )
        
        val sampleBoard = Board(
            id = 1,
            name = "Handmade Decor",
            photos = samplePhotos.toMutableList()
        )
        
        BoardCard(
            board = sampleBoard,
            onClick = {}
        )
    }
}
