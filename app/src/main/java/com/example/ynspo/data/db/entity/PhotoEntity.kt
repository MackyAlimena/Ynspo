package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: String,
    val description: String?,
    val smallUrl: String,
    val regularUrl: String,
    val fullUrl: String,
    val savedAt: Long = System.currentTimeMillis()
)
