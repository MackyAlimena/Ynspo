package com.example.ynspo.ui.boards

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.repository.Board
import com.example.ynspo.data.repository.BoardsRepository
import com.example.ynspo.notification.NotificationReceiver
import com.example.ynspo.notification.ScheduleNotificationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    private val repository: BoardsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _boards = MutableStateFlow<List<Board>>(repository.getBoards())
    val boards: StateFlow<List<Board>> = _boards

    fun addToBoard(boardId: Int, photo: UnsplashPhoto) {
        repository.addPhotoToBoard(boardId, photo)
        _boards.value = repository.getBoards()
    }


    fun getPhotosForBoard(boardId: Int): List<UnsplashPhoto> {
        return repository.getBoardPhotos(boardId)
    }    fun getBoardById(id: Int): Board? {
        return _boards.value.firstOrNull { it.id == id }
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
