package com.example.ynspo.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingS)
    ) {
        items(photos) { photo ->
            PinterestGridItem(
                photo = photo,
                onClick = { onPhotoClick(photo) }
            )
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
        
        PinterestGrid(
            photos = mockPhotos,
            onPhotoClick = {}
        )
    }
}