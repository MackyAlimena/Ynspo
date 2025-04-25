package com.example.ynspo.ui.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.components.TopBar
import com.example.ynspo.ui.theme.BackgroundColor
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ynspo.ui.home.HomeViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val photos = viewModel.photos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.search("crafts")
    }

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        TopBar()

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            items(photos.value) { photo ->
                Card(
                    colors = CardDefaults.cardColors(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((150..250).random().dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = photo.urls.small),
                        contentDescription = photo.description ?: "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}