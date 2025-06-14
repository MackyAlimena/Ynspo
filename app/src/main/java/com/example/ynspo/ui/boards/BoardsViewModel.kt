package com.example.ynspo.ui.boards

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.repository.Board
import com.example.ynspo.data.repository.BoardsRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: BoardsRoomRepository
) : ViewModel() {

    // Obtenemos los tableros desde el repositorio con Room
    val boards: LiveData<List<Board>> = repository.boards

    // Función para añadir una foto a un tablero
    fun addToBoard(boardId: Int, photo: UnsplashPhoto) {
        viewModelScope.launch {
            repository.addPhotoToBoard(boardId.toLong(), photo)
        }
    }

    // Función para crear un nuevo tablero
    fun createBoard(name: String) {
        viewModelScope.launch {
            repository.createBoard(name)
        }
    }

    // Función para eliminar un tablero
    fun deleteBoard(board: Board) {
        viewModelScope.launch {
            repository.deleteBoard(board)
        }
    }

    // Función para obtener un tablero por su ID
    suspend fun getBoardById(id: Int): Board? {
        return repository.getBoardById(id.toLong())
    }

    // Función para obtener las fotos de un tablero específico
    fun getBoardPhotos(boardId: Int): LiveData<List<UnsplashPhoto>> {
        return repository.getBoardPhotos(boardId.toLong())
    }
}
