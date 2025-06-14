package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "boards")
data class BoardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val creationDate: Long = System.currentTimeMillis()
)
