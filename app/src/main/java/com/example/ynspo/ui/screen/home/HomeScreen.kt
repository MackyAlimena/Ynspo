package com.example.ynspo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val photos by viewModel.photos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.search("crafts")
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(photos) { photo ->
            Image(
                painter = rememberAsyncImagePainter(photo.urls.small),
                contentDescription = photo.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
