package com.example.ynspo.data.model

data class Board(
    val id: Long,
    val name: String,
    val photos: MutableList<InspirationItem> = mutableListOf()
)
