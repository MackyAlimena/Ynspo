package com.example.ynspo.ui.profile

data class UserProfile(
    val name: String,
    val photoUrl: String?,
    val bio: String,
    val hobbies: List<String>,
)
