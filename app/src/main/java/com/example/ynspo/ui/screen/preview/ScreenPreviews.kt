package com.example.ynspo.ui.screen.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ynspo.R
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.Board
import com.example.ynspo.ui.components.board.BoardCard
import com.example.ynspo.ui.components.card.PhotoCard
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.YnspoTheme



// Sample data for previews
private val samplePhotos = listOf(
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
    ),
    UnsplashPhoto(
        id = "3",
        description = "Sample Photo 3",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1516214104703-d870798883c5?w=200",
            regular = "https://images.unsplash.com/photo-1516214104703-d870798883c5?w=400",
            full = "https://images.unsplash.com/photo-1516214104703-d870798883c5"
        )
    ),
    UnsplashPhoto(
        id = "4",
        description = "Sample Photo 4",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1551269901-5c5e14c25df7?w=200",
            regular = "https://images.unsplash.com/photo-1551269901-5c5e14c25df7?w=400",
            full = "https://images.unsplash.com/photo-1551269901-5c5e14c25df7"
        )
    )
)

private val sampleBoards = listOf(
    Board(
        id = 1,
        name = "Handmade Decor",
        photos = samplePhotos.take(2).toMutableList()
    ),
    Board(
        id = 2,
        name = "Knitting Ideas",
        photos = samplePhotos.takeLast(2).toMutableList()
    ),
    Board(
        id = 3,
        name = "Summer Vibes",
        photos = samplePhotos.toMutableList()
    )
)

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    YnspoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundColor
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                HomeHeaderPreview()
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                    ) {
                        samplePhotos.forEachIndexed { index, photo ->
                            if (index % 2 == 0) {
                                PhotoCard(
                                    photo = photo,
                                    onClick = {}
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(Dimens.PaddingS))
                    
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                    ) {
                        samplePhotos.forEachIndexed { index, photo ->
                            if (index % 2 == 1) {
                                PhotoCard(
                                    photo = photo,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BoardsScreenPreview() {
    YnspoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundColor
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL)
            ) {
                Text(
                    text = stringResource(id = R.string.boards_title),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = Dimens.PaddingL)
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    sampleBoards.forEach { board ->
                        BoardCard(
                            board = board,
                            onClick = {}
                        )
                    }
                }
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
