package com.example.ynspo.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens

/**
 * PinterestGrid displays a grid of images in a staggered layout.
 * Each image is clickable and will trigger the provided onPhotoClick callback.
 *
 * @param photos The list of UnsplashPhoto objects to display
 * @param onPhotoClick Callback function when a photo is clicked
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinterestGrid(
    photos: List<UnsplashPhoto>,
    onPhotoClick: (UnsplashPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = Dimens.PaddingS,
        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS),
        contentPadding = PaddingValues(bottom = Dimens.BottomBarPadding), // Espacio para bottom bar
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingS)
    ) {
        items(photos) { photo ->
            EnhancedPinterestGridItem(
                photo = photo,
                onClick = { onPhotoClick(photo) }
            )
        }
    }
}

@Composable
fun EnhancedPinterestGridItem(
    photo: UnsplashPhoto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(Dimens.CornerRadiusM),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationM),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagen principal
            AsyncImage(
                model = photo.urls.small,
                contentDescription = photo.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimens.PinCardMinHeight, max = Dimens.PinCardMaxHeight)
                    .clip(RoundedCornerShape(Dimens.CornerRadiusM))
            )
            
            // Gradient overlay negro sutil en la parte inferior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.GradientOverlayHeight)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.2f),
                                Color.Black.copy(alpha = 0.6f)
                            ),
                            startY = 0f,
                            endY = 150f
                        ),
                        shape = RoundedCornerShape(bottomStart = Dimens.CornerRadiusM, bottomEnd = Dimens.CornerRadiusM)
                    )
            )
            
            // Texto de descripción (si existe)
            photo.description?.let { description ->
                if (description.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(Dimens.PaddingM)
                    ) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

// Función legacy mantenida para compatibilidad
@Composable
fun PinterestGridItem(
    photo: UnsplashPhoto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(Dimens.PaddingS),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = photo.urls.small,
            contentDescription = photo.description,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PinterestGridPreview() {
    YnspoTheme {
        val mockPhotos = listOf(
            UnsplashPhoto(
                id = "1",
                description = "Beautiful handmade crafts with natural materials",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1523567830207-96731740fa71?w=200",
                    regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71?w=400",
                    full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
                )
            ),
            UnsplashPhoto(
                id = "2",
                description = "Creative DIY inspiration",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611?w=200",
                    regular = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611?w=400",
                    full = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611"
                )
            )
        )
        
        PinterestGrid(
            photos = mockPhotos,
            onPhotoClick = {}
        )
    }
}