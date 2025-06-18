package com.example.ynspo.ui.screen.boards

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.repository.Board
import com.example.ynspo.data.repository.BoardsRepository
import com.example.ynspo.notification.NotificationReceiver
import com.example.ynspo.notification.ScheduleNotificationViewModel
import com.example.ynspo.data.model.Board
import com.example.ynspo.data.repository.BoardsRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: BoardsRepository,
    @ApplicationContext private val context: Context
    private val repository: BoardsRoomRepository
) : ViewModel() {

    // Obtenemos los tableros desde el repositorio con Room
    val boards: LiveData<List<Board>> = repository.boards    // Función para añadir una foto a un tablero
    fun addToBoard(boardId: Long, photo: UnsplashPhoto) {
        viewModelScope.launch {
            repository.addPhotoToBoard(boardId, photo)
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
    /**
     * Envía una notificación cuando se guarda un pin en un tablero
     */
    fun sendSavedPinNotification(boardName: String, photoDescription: String) {
        val notificationViewModel = ScheduleNotificationViewModel(context)

        val title = "Pin guardado en $boardName"
        val message = if (photoDescription.isNotEmpty()) {
            "Has guardado '$photoDescription' en tu tablero"
        } else {
            "Has guardado una nueva inspiración en tu tablero"
        }

        // Programar la notificación para que aparezca inmediatamente
        notificationViewModel.scheduleNotification(
            delayInSeconds = 1,
            title = title,
            message = message
        )
    }
}
