package com.example.ynspo.ui.components.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.YnspoTheme


@Composable
fun PhotoCard(
    photo: UnsplashPhoto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingXS)
            .clickable { onClick() },        shape = RoundedCornerShape(Dimens.PaddingM),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.PaddingXS)
    ) {
        AsyncImage(
            model = photo.urls.small,
            contentDescription = photo.description ?: "Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.PaddingM))
                .aspectRatio(
                    ratio = 0.8f, 
                    matchHeightConstraintsFirst = true
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoCardPreview() {
    YnspoTheme {
        val samplePhoto = UnsplashPhoto(
            id = "1",
            description = "Sample Photo",
            urls = UnsplashPhotoUrls(
                small = "https://images.unsplash.com/photo-1523567830207-96731740fa71?w=200",
                regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71?w=400",
                full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
            )
        )
        
        PhotoCard(
            photo = samplePhoto,
            onClick = {},
            modifier = Modifier.padding(Dimens.PaddingL)
        )
    }
}
