package com.example.ynspo.ui.components

import androidx.lifecycle.ViewModel
import com.example.ynspo.data.model.UnsplashPhoto
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {
    var selectedPhoto: UnsplashPhoto? = null
}
