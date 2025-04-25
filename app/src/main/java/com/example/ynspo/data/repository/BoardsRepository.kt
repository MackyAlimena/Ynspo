package com.example.ynspo.data.repository

import com.example.ynspo.data.model.UnsplashPhoto
import javax.inject.Inject
import javax.inject.Singleton

data class Board(
    val id: Int,
    val name: String,
    val photos: MutableList<UnsplashPhoto> = mutableListOf()
)

@Singleton
class BoardsRepository @Inject constructor() {
    private val boards = mutableListOf(
        Board(id = 1, name = "Handmade Decor"),
        Board(id = 2, name = "Knitting Ideas"),
        Board(id = 3, name = "Summer Vibes")
    )

    fun getBoards(): List<Board> = boards

    fun addPhotoToBoard(boardId: Int, photo: UnsplashPhoto) {
        boards.find { it.id == boardId }?.photos?.add(photo)
    }

    fun getBoardPhotos(boardId: Int): List<UnsplashPhoto> {
        return boards.find { it.id == boardId }?.photos ?: emptyList()
    }
}
