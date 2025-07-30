package com.example.ynspo.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.toInspirationItem

import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens


/**
 * PinterestGrid displays a grid of inspiration items in a staggered layout.
 * Each item is clickable and will trigger the provided onItemClick callback.
 *
 * @param inspirationItems The list of InspirationItem objects to display
 * @param onItemClick Callback function when an item is clicked
 * @param onDeleteItem Optional callback function when delete button is clicked
 * @param showDeleteButtons Whether to show delete buttons on items
 * @param modifier Modifier for the grid
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinterestGrid(
    inspirationItems: List<InspirationItem>,
    onItemClick: (InspirationItem) -> Unit,
    onDeleteItem: ((InspirationItem) -> Unit)? = null,
    showDeleteButtons: Boolean = false,
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
        items(inspirationItems) { item ->
            EnhancedPinterestGridItem(
                inspirationItem = item,
                onClick = { onItemClick(item) },
                onDelete = onDeleteItem?.let { { it(item) } },
                showDeleteButton = showDeleteButtons
            )
        }
    }
}

@Composable
fun EnhancedPinterestGridItem(
    inspirationItem: InspirationItem,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null,
    showDeleteButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Log para debuggear las URLs de las imágenes
    LaunchedEffect(inspirationItem.id) {
        android.util.Log.d("PinterestGrid", "Renderizando item: ${inspirationItem.id}")
        android.util.Log.d("PinterestGrid", "URLs: small=${inspirationItem.urls.small}, regular=${inspirationItem.urls.regular}, full=${inspirationItem.urls.full}")
        android.util.Log.d("PinterestGrid", "Descripción: ${inspirationItem.description}")
        android.util.Log.d("PinterestGrid", "Es UserPin: ${inspirationItem.isUserPin}")
        

    }
    
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
                model = inspirationItem.urls.small,
                contentDescription = inspirationItem.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = Dimens.PinCardMinHeight, max = Dimens.PinCardMaxHeight)
                    .clip(RoundedCornerShape(Dimens.CornerRadiusM)),
                onSuccess = {
                    android.util.Log.d("PinterestGrid", "✅ Imagen cargada exitosamente: ${inspirationItem.urls.small}")
                },
                onError = {
                    android.util.Log.e("PinterestGrid", "❌ Error cargando imagen: ${inspirationItem.urls.small}")
                    android.util.Log.e("PinterestGrid", "❌ Error details: $it")
                },
                onLoading = {
                    android.util.Log.d("PinterestGrid", "⏳ Cargando imagen: ${inspirationItem.urls.small}")
                }
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
            
            // Texto de descripción
            inspirationItem.description?.let { description ->
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
            
            // Indicador de pin de usuario
            if (inspirationItem.isUserPin) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Dimens.PaddingS)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            CircleShape
                        )
                        .size(24.dp)
                ) {
                    Text(
                        text = "👤",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            
            // Botón de borrar (solo si showDeleteButton es true)
            if (showDeleteButton && onDelete != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(Dimens.PaddingS)
                        .background(
                            MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                            CircleShape
                        )
                        .size(32.dp)
                        .clickable { onDelete() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar pin",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(18.dp)
                    )
                }
            }
        }
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
        
        val inspirationItems = mockPhotos.map { it.toInspirationItem() }
        PinterestGrid(
            inspirationItems = inspirationItems,
            onItemClick = {}
        )
    }
}