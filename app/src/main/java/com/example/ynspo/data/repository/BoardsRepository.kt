package com.example.ynspo.data.repository

import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.Board
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardsRepository @Inject constructor() {    private val boards = mutableListOf(
        Board(id = 1L, name = "Handmade Decor"),
        Board(id = 2L, name = "Knitting Ideas"),
        Board(id = 3L, name = "Summer Vibes")
    )

    fun getBoards(): List<Board> = boards

    fun addPhotoToBoard(boardId: Long, photo: UnsplashPhoto) {
        boards.find { it.id == boardId }?.photos?.add(photo)
    }

    fun getBoardPhotos(boardId: Long): List<UnsplashPhoto> {
        return boards.find { it.id == boardId }?.photos ?: emptyList()
    }
}
