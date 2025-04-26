package com.example.ynspo.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    private val _photos = MutableStateFlow<List<UnsplashPhoto>>(emptyList())
    val photos: StateFlow<List<UnsplashPhoto>> = _photos

    fun search(query: String) {
        viewModelScope.launch {
            val response = repository.searchPhotos(query)
            _photos.value = response.results
        }
    }
}
