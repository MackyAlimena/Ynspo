package com.example.ynspo.ui.screen.pins

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.R
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.ui.components.dialog.AuthenticatedSaveToBoardDialog
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.SelectedColor
import com.example.ynspo.ui.theme.YnspoTheme


@Composable
fun PinImage(
    photo: UnsplashPhoto,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = rememberAsyncImagePainter(photo.urls.regular),
            contentDescription = photo.description,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = Dimens.ImageHeightM * 3.5f)
        )
    }
}

@Composable
fun PinActions(
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingL)
    ) {        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(Dimens.ProfileImageSize * 0.56f)
                .background(
                    SelectedColor.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = DetailColor,
                modifier = Modifier.size(Dimens.PaddingXL + Dimens.PaddingXS)
            )
        }
          IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .size(Dimens.ProfileImageSize * 0.56f)
                .background(
                    SelectedColor.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = DetailColor,
                modifier = Modifier.size(Dimens.PaddingXL + Dimens.PaddingXS)
            )
        }
    }
}

@Composable
fun PinDescription(
    photo: UnsplashPhoto,
    modifier: Modifier = Modifier
) {    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingL)
    ) {
        if (!photo.description.isNullOrEmpty()) {
            Text(
                text = photo.description,
                style = MaterialTheme.typography.bodyLarge,
                color = DetailColor
            )
        } else {
            Text(
                text = "No description available",
                style = MaterialTheme.typography.bodyMedium,
                color = DetailColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PinComponentsPreview() {
    YnspoTheme {
        Surface(
            color = BackgroundColor,
            modifier = Modifier.fillMaxSize()
        ) {
            val samplePhoto = UnsplashPhoto(
                id = "1",
                description = "A beautiful handmade craft item with natural materials, demonstrating sustainable crafting techniques.",
                urls = UnsplashPhotoUrls(
                    small = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
                    regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
                    full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
                )
            )
            
            Column {
                PinImage(photo = samplePhoto)
                PinActions(
                    onFavoriteClick = {},
                    onShareClick = {}
                )
                PinDescription(photo = samplePhoto)
            }
        }
    }
}
