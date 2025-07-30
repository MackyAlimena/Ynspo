package com.example.ynspo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.repository.CombinedPinsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class HomeTab {
    FOR_YOU, EXPLORE
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val combinedRepository: CombinedPinsRepository
) : ViewModel() {

    private val _inspirationItems = MutableStateFlow<List<InspirationItem>>(emptyList())
    val inspirationItems: StateFlow<List<InspirationItem>> = _inspirationItems
    
    private val _currentTab = MutableStateFlow(HomeTab.FOR_YOU)
    val currentTab: StateFlow<HomeTab> = _currentTab
    
    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private var currentPage = 1
    private val craftsQueries = listOf("crafts", "diy", "handmade", "art", "creative", "decoration")
    private var currentQueryIndex = 0

    // Sugerencias de búsqueda populares
    val searchSuggestions = listOf(
        "Decoración", "DIY", "Manualidades", "Arte", "Cocina", "Jardín", 
        "Moda", "Vintage", "Minimalista", "Industrial"
    )

    fun setCurrentTab(tab: HomeTab) {
        _currentTab.value = tab
        if (tab == HomeTab.FOR_YOU) {
            refreshPhotos()
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val combinedPins = combinedRepository.searchCombinedPins(query)
                _inspirationItems.value = combinedPins
                
                // Agregar al historial si no está vacío y no existe ya
                if (query.isNotBlank() && !_searchHistory.value.contains(query)) {
                    val newHistory = listOf(query) + _searchHistory.value.take(4)
                    _searchHistory.value = newHistory
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshPhotos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val combinedPins = combinedRepository.getCombinedPinsForYou()
                _inspirationItems.value = combinedPins
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearSearchHistory() {
        _searchHistory.value = emptyList()
    }
}
