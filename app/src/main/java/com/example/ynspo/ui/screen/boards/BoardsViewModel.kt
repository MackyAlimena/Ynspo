package com.example.ynspo.ui.boards

import androidx.lifecycle.ViewModel
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.repository.Board
import com.example.ynspo.data.repository.BoardsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: BoardsRepository
) : ViewModel() {

    private val _boards = MutableStateFlow<List<Board>>(repository.getBoards())
    val boards: StateFlow<List<Board>> = _boards

    fun addToBoard(boardId: Int, photo: UnsplashPhoto) {
        repository.addPhotoToBoard(boardId, photo)
        _boards.value = repository.getBoards()
    }


    fun getPhotosForBoard(boardId: Int): List<UnsplashPhoto> {
        return repository.getBoardPhotos(boardId)
    }

    fun getBoardById(id: Int): Board? {
        return _boards.value.firstOrNull { it.id == id }
    }

}
