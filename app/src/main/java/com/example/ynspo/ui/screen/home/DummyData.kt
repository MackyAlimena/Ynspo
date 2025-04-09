package com.example.ynspo.ui.screen.home

import com.example.ynspo.R


data class InspirationCard(
    val imageRes: Int,
    val title: String
)

val inspirationList = listOf(
    InspirationCard(R.drawable.image1, "Pools that make us dream"),
    InspirationCard(R.drawable.image2, "Dreamy creek"),
    InspirationCard(R.drawable.image3, "Summer vibe inspiration"),
    InspirationCard(R.drawable.image4, "Beach style trees")
)
