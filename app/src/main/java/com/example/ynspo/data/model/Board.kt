package com.example.ynspo.data.model

import com.example.ynspo.data.model.UnsplashPhoto

data class Board(
    val id: Long,
    val name: String,
    val photos: MutableList<UnsplashPhoto> = mutableListOf()
)
