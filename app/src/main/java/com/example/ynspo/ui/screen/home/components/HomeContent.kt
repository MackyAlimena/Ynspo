package com.example.ynspo.ui.screen.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens


@Composable
fun HomeContent(
    photos: List<UnsplashPhoto>,
    onPhotoClick: (UnsplashPhoto) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.PaddingS)
    ) {
        // The Pinterest grid displays the photos in a staggered grid
        PinterestGrid(
            photos = photos,
            onPhotoClick = onPhotoClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    YnspoTheme {
        // Sample data for preview
        val samplePhotos = listOf(
            UnsplashPhoto(
                id = "1",
                description = "Sample photo 1",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
                    regular = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
                    full = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5"
                )
            ),
            UnsplashPhoto(
                id = "2",
                description = "Sample photo 2",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
                    regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
                    full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
                )
            )
        )
        
        HomeContent(
            photos = samplePhotos,
            onPhotoClick = {}
        )
    }
}
