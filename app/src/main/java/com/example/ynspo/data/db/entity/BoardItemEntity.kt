package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "board_items")
data class BoardItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val boardId: Long,
    val itemId: String, // ID del item (UnsplashPhoto.id o UserPin.id)
    val itemType: String, // "unsplash" o "user_pin"
    val savedAt: Long = System.currentTimeMillis()
) 