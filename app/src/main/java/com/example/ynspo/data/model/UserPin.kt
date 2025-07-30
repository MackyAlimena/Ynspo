package com.example.ynspo.data.model

data class UserPin(
    val id: String,
    val description: String?,
    val urls: UserPinUrls,
    val userId: String,
    val userName: String?,
    val userPhotoUrl: String?,
    val hashtags: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isUserPin: Boolean = true // Para distinguir de Unsplash
)

data class UserPinUrls(
    val small: String,
    val regular: String,
    val full: String
) 