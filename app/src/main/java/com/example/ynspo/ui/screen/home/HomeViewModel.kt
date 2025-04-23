package com.example.ynspo.ui.screen.home

import androidx.lifecycle.ViewModel
import com.example.ynspo.R
import com.example.ynspo.data.model.InspirationCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _cards = MutableStateFlow(
        listOf(
            InspirationCard(R.drawable.image1, "Dreamy beach"),
            InspirationCard(R.drawable.image2, "Sunset coast"),
            InspirationCard(R.drawable.image3, "Tropical vibes"),
            InspirationCard(R.drawable.image4, "Island path")
        )
    )
    val cards: StateFlow<List<InspirationCard>> = _cards
}