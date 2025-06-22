package com.example.ynspo.ui.components.board

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.Board
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.SelectedColor
import com.example.ynspo.ui.theme.YnspoTheme


@Composable
fun BoardCard(
    board: Board,
    onClick: (Board) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(board) }
            .padding(Dimens.PaddingS),
        colors = CardDefaults.cardColors(containerColor = SelectedColor.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(Dimens.PaddingM)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingM)) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)) {
                board.photos.take(3).forEach { photo ->
                    BoardThumbnail(photo = photo)
                }
            }
            
            Spacer(modifier = Modifier.height(Dimens.PaddingS))
            
            Text(
                text = board.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = "${board.photos.size} pins",
                style = MaterialTheme.typography.bodySmall
            )
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
