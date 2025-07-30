package com.example.ynspo.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ynspo.ui.theme.Dimens
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.components.SharedViewModel
import com.example.ynspo.ui.screen.home.components.HomeContent
import com.example.ynspo.ui.screen.home.components.ModernHomeHeader
import androidx.compose.material3.MaterialTheme
import com.example.ynspo.data.model.InspirationItem


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val inspirationItems by viewModel.inspirationItems.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val searchHistory by viewModel.searchHistory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Initially load content based on current tab
    LaunchedEffect(Unit) {
        if (currentTab == HomeTab.FOR_YOU) {
            viewModel.refreshPhotos()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Modern Header with tabs and search
        ModernHomeHeader(
            currentTab = currentTab,
            onTabChange = { tab -> 
                viewModel.setCurrentTab(tab)
                if (tab == HomeTab.EXPLORE && inspirationItems.isEmpty()) {
                    // Si va a Explore y no hay items, hacer una búsqueda inicial
                    viewModel.search("inspiration")
                }
            },
            onSearch = { query -> viewModel.search(query) },
            searchHistory = searchHistory,
            searchSuggestions = viewModel.searchSuggestions,
            onClearHistory = { viewModel.clearSearchHistory() }
        )
        
        // Content - Pinterest grid of photos with loading state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading && inspirationItems.isEmpty()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(Dimens.PaddingXXL)
                )
            } else {
                HomeContent(
                    inspirationItems = inspirationItems,
                    onItemClick = { item ->
                        // Store the selected item in the shared view model
                        sharedViewModel.selectedPhoto = item
                        // Navigate to the pin detail screen
                        navController.navigate("pinDetail")
                    }
                )
            }
        }
    }
}


