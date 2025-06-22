package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val photoUrl: String?,
    val bio: String,
    val lastUpdated: Long = System.currentTimeMillis(),
    val darkModeEnabled: Boolean = false
)
