package com.example.ynspo.ui.screen.boards.board_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.Dimens

@Composable
fun BoardDetailScreen(
    boardId: Int,
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel()
) {
    val board = boardsViewModel.getBoardById(boardId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(Dimens.PaddingL)
    ) {
        if (board != null) {
            Text(
                text = board.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = Dimens.PaddingL)
            )
        }

        if (board != null) {
            PinterestGrid(
                photos = board.photos,
                onPhotoClick = { photo ->
                    navController.navigate("pinDetail/${photo.urls.small}")
                }
            )
        }
    }
}
