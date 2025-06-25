package com.example.ynspo.ui.screen.boards.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ynspo.R
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.Board
import com.example.ynspo.ui.components.board.BoardCard
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens


@Composable
fun BoardsList(
    boards: List<Board>,
    onBoardClick: (Board) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingL)
    ) {
        Text(
            text = stringResource(R.string.boards_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = Dimens.PaddingL)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingL),
            modifier = Modifier.fillMaxSize()
        ) {
            items(boards) { board ->
                BoardCard(
                    board = board,
                    onClick = { onBoardClick(board) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardsListPreview() {
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
        
        val sampleBoards = listOf(
            Board(
                id = 1,
                name = "Handmade Decor",
                photos = samplePhotos.toMutableList()
            ),
            Board(
                id = 2,
                name = "Knitting Ideas",
                photos = samplePhotos.toMutableList()
            ),
            Board(
                id = 3,
                name = "Summer Vibes",
                photos = samplePhotos.toMutableList()
            )
        )
        
        BoardsList(
            boards = sampleBoards,
            onBoardClick = {}
        )
    }
}
