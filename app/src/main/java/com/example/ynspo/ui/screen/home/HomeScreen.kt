package com.example.ynspo.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.components.SharedViewModel
import com.example.ynspo.ui.screen.home.components.HomeContent
import com.example.ynspo.ui.screen.home.components.HomeHeader
import com.example.ynspo.ui.theme.BackgroundColor

/**
 * Main home screen of the application
 */
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val photos = viewModel.photos.collectAsState()

    // Initially search for crafts
    LaunchedEffect(Unit) {
        viewModel.search("crafts")
    }

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        // Header
        HomeHeader()
        
        // Content - Pinterest grid of photos
        HomeContent(
            photos = photos.value,
            onPhotoClick = { photo ->
                // Store the selected photo in the shared view model
                sharedViewModel.selectedPhoto = photo
                // Navigate to the pin detail screen
                navController.navigate("pinDetail")
            }
        )
    }
}


