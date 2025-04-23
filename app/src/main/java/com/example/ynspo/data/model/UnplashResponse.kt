package com.example.ynspo.data.model

data class UnsplashResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)
