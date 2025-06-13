package com.example.ynspo.ui.components.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ynspo.R
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.YnspoTheme

/**
 * A staggered grid layout for displaying photos in a Pinterest-style grid.
 *
 * @param photos List of photos to display
 * @param onPhotoClick Callback when a photo is clicked
 * @param modifier Modifier for the grid
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PinterestGrid(
    photos: List<UnsplashPhoto>,
    onPhotoClick: (UnsplashPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    if (photos.isEmpty()) {
        // Empty state
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Text(
                text = "No photos available",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),            
            verticalItemSpacing = Dimens.GridSpacing,
            horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
            modifier = modifier
                .fillMaxSize()
                .padding(Dimens.PaddingS)
        ) {
            items(photos) { photo ->
                PinterestGridItem(photo = photo, onClick = { onPhotoClick(photo) })
            }
        }
    }
}


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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(
                    ratio = when {
                        photo.id.hashCode() % 3 == 0 -> 0.9f
                        photo.id.hashCode() % 3 == 1 -> 1.2f
                        else -> 1.0f
                    }
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PinterestGridPreview() {
    YnspoTheme {
        val samplePhotos = listOf(
            UnsplashPhoto(
                id = "1",
                description = "Sample Photo 1",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
                    regular = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
                    full = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5"
                )
            ),
            UnsplashPhoto(
                id = "2",
                description = "Sample Photo 2",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
                    regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
                    full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
                )
            )
        )

        Box(modifier = Modifier.size(Dimens.PinterestGrid300, Dimens.PinterestGrid500)) {
            PinterestGrid(
                photos = samplePhotos,
                onPhotoClick = {}
            )
        }
    }
}
