package com.example.ynspo.ui.screen.boards

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.Board
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.repository.BoardsMixedRepository
import com.example.ynspo.data.repository.UserPinsRepository
import com.example.ynspo.notification.ScheduleNotificationViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mixedRepository: BoardsMixedRepository,
    private val userPinsRepository: UserPinsRepository
) : ViewModel() {

    // Obtenemos los tableros desde el repositorio mixto
    val boards: LiveData<List<Board>> = mixedRepository.boards

    // Función para añadir una foto a un tablero (legacy - solo Unsplash)
    fun addToBoard(boardId: Long, photo: UnsplashPhoto) {
        viewModelScope.launch {
            mixedRepository.addPhotoToBoard(boardId, photo)
        }
    }

    // Función para añadir un item de inspiración a un tablero (nuevo - ambos tipos)
    fun addInspirationItemToBoard(boardId: Long, inspirationItem: InspirationItem) {
        viewModelScope.launch {
            mixedRepository.addInspirationItemToBoard(boardId, inspirationItem)
        }
    }

    // Función para crear un nuevo tablero
    fun createBoard(name: String) {
        viewModelScope.launch {
            mixedRepository.createBoard(name)
        }
    }

    // Función para eliminar un tablero
    fun deleteBoard(board: Board) {
        viewModelScope.launch {
            mixedRepository.deleteBoard(board)
        }
    }

    // Función para obtener un tablero por su ID
    suspend fun getBoardById(id: Int): Board? {
        return mixedRepository.getBoardById(id.toLong())
    }

    // Función para obtener las fotos de un tablero específico (legacy - solo Unsplash)
    fun getBoardPhotos(boardId: Int): LiveData<List<UnsplashPhoto>> {
        return mixedRepository.getBoardPhotos(boardId.toLong())
    }

    // Función para obtener los items de inspiración de un tablero específico (nuevo - ambos tipos)
    fun getBoardInspirationItems(boardId: Int): LiveData<List<InspirationItem>> {
        return mixedRepository.getBoardInspirationItems(boardId.toLong())
    }

    // Función para obtener boards con photos cargados
    suspend fun getBoardsWithPhotos(): List<Board> {
        return mixedRepository.getBoardsWithPhotos()
    }

    // Función para borrar un item de inspiración de un tablero
    fun removeInspirationItemFromBoard(boardId: Long, itemId: String, itemType: String) {
        viewModelScope.launch {
            mixedRepository.removeInspirationItemFromBoard(boardId, itemId, itemType)
        }
    }

    // Función para borrar todos los UserPins (tanto de Room como de Firestore)
    fun deleteAllUserPins() {
        viewModelScope.launch {
            val result = userPinsRepository.deleteAllUserPins()
            if (result.isSuccess) {
                android.util.Log.d("BoardsViewModel", "✅ Todos los UserPins borrados exitosamente")
            } else {
                android.util.Log.e("BoardsViewModel", "❌ Error borrando UserPins: ${result.exceptionOrNull()?.message}")
            }
        }
    }


    //Envía una notificación cuando se guarda un pin en un tablero
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
