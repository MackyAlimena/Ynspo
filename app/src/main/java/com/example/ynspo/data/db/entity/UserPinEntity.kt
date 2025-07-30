package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_pins")
data class UserPinEntity(
    @PrimaryKey
    val id: String,
    val description: String?,
    val smallUrl: String,
    val regularUrl: String,
    val fullUrl: String,
    val userId: String,
    val userName: String?,
    val userPhotoUrl: String?,
    val hashtags: String,
    val keywords: String,
    val createdAt: Long = System.currentTimeMillis(),
    val savedAt: Long = System.currentTimeMillis()
) 