package com.example.ynspo.data.model

data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val urls: UnsplashPhotoUrls
)

data class UnsplashPhotoUrls(
    val small: String,
    val regular: String,
    val full: String
)
