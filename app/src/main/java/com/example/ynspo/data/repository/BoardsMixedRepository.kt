package com.example.ynspo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.db.mapper.BoardMapper
import com.example.ynspo.data.db.mapper.PhotoMapper
import com.example.ynspo.data.db.mapper.UserPinMapper
import com.example.ynspo.data.db.mapper.BoardItemMapper
import com.example.ynspo.data.model.Board
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.UserPin
import com.example.ynspo.data.model.UserPinUrls
import com.example.ynspo.data.model.toInspirationItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardsMixedRepository @Inject constructor(
    private val database: YnspoDatabase
) {
    // LiveData que se actualiza cuando cambian los boards o los board_items
    private val boardsWithItems: LiveData<List<Board>> = MediatorLiveData<List<Board>>().apply {
        addSource(database.boardDao().getAllBoards()) { boardEntities ->
            android.util.Log.d("BoardsMixedRepository", "Boards actualizados: ${boardEntities.size} boards")
            val boards = BoardMapper.fromEntities(boardEntities)
            val boardsWithPhotos = boards.map { board ->
                val inspirationItems = kotlinx.coroutines.runBlocking {
                    getBoardInspirationItemsSync(board.id)
                }
                android.util.Log.d("BoardsMixedRepository", "Board '${board.name}' tiene ${inspirationItems.size} items")
                board.copy(photos = inspirationItems.toMutableList())
            }
            postValue(boardsWithPhotos)
        }
        
        // Observar cambios en board_items para actualizar cuando se agreguen pins
        addSource(database.boardItemDao().getAllBoardItems()) { boardItems ->
            android.util.Log.d("BoardsMixedRepository", "Board items actualizados: ${boardItems.size} items")
            // Siempre recalcular todos los boards cuando cambian los items
            kotlinx.coroutines.runBlocking {
                val boardEntities = database.boardDao().getAllBoardsSync()
                val boards = BoardMapper.fromEntities(boardEntities)
                val updatedBoards = boards.map { board ->
                    val inspirationItems = getBoardInspirationItemsSync(board.id)
                    android.util.Log.d("BoardsMixedRepository", "Board '${board.name}' actualizado con ${inspirationItems.size} items")
                    board.copy(photos = inspirationItems.toMutableList())
                }
                postValue(updatedBoards)
            }
        }
    }

    // Obtener todos los tableros
    val boards: LiveData<List<Board>> = boardsWithItems

    // Obtener todos los tableros con sus photos cargados
    suspend fun getBoardsWithPhotos(): List<Board> {
        val boardEntities = database.boardDao().getAllBoardsSync()
        val boards = BoardMapper.fromEntities(boardEntities)
        return boards.map { board ->
            val inspirationItems = getBoardInspirationItemsSync(board.id)
            board.copy(photos = inspirationItems.toMutableList())
        }
    }

    // Versión sincrónica de getBoardInspirationItems
    private suspend fun getBoardInspirationItemsSync(boardId: Long): List<InspirationItem> {
        val boardItems = database.boardItemDao().getBoardItemsSync(boardId)
        android.util.Log.d("BoardsMixedRepository", "Board $boardId tiene ${boardItems.size} board items")
        
        val inspirationItems = mutableListOf<InspirationItem>()

        for (boardItem in boardItems) {
            android.util.Log.d("BoardsMixedRepository", "Procesando board item: ${boardItem.itemId} (tipo: ${boardItem.itemType})")
            when (boardItem.itemType) {
                "unsplash" -> {
                    val photoEntity = database.photoDao().getPhotoById(boardItem.itemId)
                    photoEntity?.let {
                        inspirationItems.add(PhotoMapper.fromEntity(it).toInspirationItem())
                        android.util.Log.d("BoardsMixedRepository", "Agregado Unsplash item: ${boardItem.itemId}")
                    }
                }
                "user_pin" -> {
                    val userPinEntity = database.userPinDao().getUserPinById(boardItem.itemId)
                    userPinEntity?.let {
                        inspirationItems.add(UserPinMapper.fromEntity(it).toInspirationItem())
                        android.util.Log.d("BoardsMixedRepository", "Agregado UserPin item: ${boardItem.itemId}")
                    }
                }
            }
        }
        
        android.util.Log.d("BoardsMixedRepository", "Board $boardId tiene ${inspirationItems.size} inspiration items")
        return inspirationItems
    }

    // Obtener los items de inspiración para un tablero específico
    fun getBoardInspirationItems(boardId: Long): LiveData<List<InspirationItem>> {
        return database.boardItemDao().getBoardItems(boardId).map { boardItems ->
            val inspirationItems = mutableListOf<InspirationItem>()

            // Usar coroutine scope para manejar las llamadas suspend
            kotlinx.coroutines.runBlocking {
                for (boardItem in boardItems) {
                    when (boardItem.itemType) {
                        "unsplash" -> {
                            val photoEntity = database.photoDao().getPhotoById(boardItem.itemId)
                            photoEntity?.let {
                                inspirationItems.add(PhotoMapper.fromEntity(it).toInspirationItem())
                            }
                        }
                        "user_pin" -> {
                            val userPinEntity = database.userPinDao().getUserPinById(boardItem.itemId)
                            userPinEntity?.let {
                                inspirationItems.add(UserPinMapper.fromEntity(it).toInspirationItem())
                            }
                        }
                    }
                }
            }
            inspirationItems
        }
    }

    // Agregar un nuevo tablero
    suspend fun createBoard(name: String): Long {
        val boardEntity = BoardMapper.toEntity(Board(id = 0, name = name))
        return database.boardDao().insertBoard(boardEntity)
    }

    // Eliminar un tablero
    suspend fun deleteBoard(board: Board) {
        val boardEntity = BoardMapper.toEntity(board)
        database.boardDao().deleteBoard(boardEntity)
        // También eliminar todos los items del board
        database.boardItemDao().deleteBoardItems(board.id)
    }

    // Agregar un item de inspiración a un tablero
    suspend fun addInspirationItemToBoard(boardId: Long, inspirationItem: InspirationItem) {
        android.util.Log.d("BoardsMixedRepository", "Agregando item '${inspirationItem.id}' al board $boardId")
        
        // Guardar el item en la base de datos local según su tipo
        when {
            inspirationItem.isUserPin -> {
                // Convertir InspirationItem a UserPin y guardarlo
                val userPin = UserPin(
                    id = inspirationItem.id,
                    description = inspirationItem.description,
                    urls = UserPinUrls(
                        small = inspirationItem.urls.small,
                        regular = inspirationItem.urls.regular,
                        full = inspirationItem.urls.full
                    ),
                    userId = "", // Valor por defecto ya que InspirationItem no tiene userId
                    userName = null,
                    userPhotoUrl = null,
                    hashtags = emptyList(),
                    keywords = emptyList(),
                    createdAt = System.currentTimeMillis(),
                    isUserPin = true
                )
                val userPinEntity = UserPinMapper.toEntity(userPin)
                database.userPinDao().insertUserPin(userPinEntity)
                android.util.Log.d("BoardsMixedRepository", "UserPin guardado en base de datos local")
            }
            else -> {
                // Es un UnsplashPhoto, convertirlo y guardarlo
                val unsplashPhoto = UnsplashPhoto(
                    id = inspirationItem.id,
                    description = inspirationItem.description,
                    urls = UnsplashPhotoUrls(
                        small = inspirationItem.urls.small,
                        regular = inspirationItem.urls.regular,
                        full = inspirationItem.urls.full
                    )
                )
                val photoEntity = PhotoMapper.toEntity(unsplashPhoto)
                database.photoDao().insertPhoto(photoEntity)
                android.util.Log.d("BoardsMixedRepository", "UnsplashPhoto guardado en base de datos local")
            }
        }
        
        // Crear la relación en board_items
        val boardItem = BoardItemMapper.toEntity(boardId, inspirationItem)
        val result = database.boardItemDao().insertBoardItem(boardItem)
        android.util.Log.d("BoardsMixedRepository", "Item agregado con ID: $result")
    }

    // Borrar un item de inspiración de un tablero
    suspend fun removeInspirationItemFromBoard(boardId: Long, itemId: String, itemType: String) {
        android.util.Log.d("BoardsMixedRepository", "Borrando item '$itemId' (tipo: $itemType) del board $boardId")
        
        // Borrar la relación de board_items
        val deletedRows = database.boardItemDao().deleteBoardItem(boardId, itemId, itemType)
        android.util.Log.d("BoardsMixedRepository", "Relación borrada: $deletedRows filas afectadas")
        
        // Opcional: También borrar el item de la base de datos local si no está en otros boards
        val itemInOtherBoards = database.boardItemDao().getBoardItemsByItemId(itemId, itemType)
        if (itemInOtherBoards.isEmpty()) {
            when (itemType) {
                "user_pin" -> {
                    database.userPinDao().deleteUserPinById(itemId)
                    android.util.Log.d("BoardsMixedRepository", "UserPin borrado de la base de datos local")
                }
                "unsplash" -> {
                    database.photoDao().deletePhotoById(itemId)
                    android.util.Log.d("BoardsMixedRepository", "UnsplashPhoto borrado de la base de datos local")
                }
            }
        } else {
            android.util.Log.d("BoardsMixedRepository", "Item mantiene en otros boards, no se borra de la base de datos local")
        }
    }

    // Agregar un UnsplashPhoto a un tablero
    suspend fun addUnsplashPhotoToBoard(boardId: Long, photo: UnsplashPhoto) {
        // Guardar la foto en la base de datos local
        val photoEntity = PhotoMapper.toEntity(photo)
        database.photoDao().insertPhoto(photoEntity)
        
        // Crear la relación en board_items
        val boardItem = BoardItemMapper.toEntity(boardId, photo.toInspirationItem())
        database.boardItemDao().insertBoardItem(boardItem)
    }

    // Agregar un UserPin a un tablero
    suspend fun addUserPinToBoard(boardId: Long, userPin: UserPin) {
        // Guardar el user pin en la base de datos local
        val userPinEntity = UserPinMapper.toEntity(userPin)
        database.userPinDao().insertUserPin(userPinEntity)
        
        // Crear la relación en board_items
        val boardItem = BoardItemMapper.toEntity(boardId, userPin.toInspirationItem())
        database.boardItemDao().insertBoardItem(boardItem)
    }

    // Agregar una foto a un tablero (legacy - solo Unsplash)
    suspend fun addPhotoToBoard(boardId: Long, photo: UnsplashPhoto) {
        addInspirationItemToBoard(boardId, photo.toInspirationItem())
    }

    // Eliminar un item de un tablero
    suspend fun removeItemFromBoard(boardId: Long, itemId: String) {
        database.boardItemDao().removeItemFromBoard(boardId, itemId)
    }

    // Verificar si un item está en un tablero
    suspend fun isItemInBoard(boardId: Long, itemId: String): Boolean {
        return database.boardItemDao().isItemInBoard(boardId, itemId)
    }

    // Obtener un tablero por su ID
    suspend fun getBoardById(boardId: Long): Board? {
        return database.boardDao().getBoardById(boardId)?.let {
            BoardMapper.fromEntity(it)
        }
    }

    // Obtener las fotos para un tablero específico (legacy - solo Unsplash)
    fun getBoardPhotos(boardId: Long): LiveData<List<UnsplashPhoto>> {
        return database.boardItemDao().getBoardItems(boardId).map { boardItems ->
            val photos = mutableListOf<UnsplashPhoto>()

            // Usar coroutine scope para manejar las llamadas suspend
            kotlinx.coroutines.runBlocking {
                for (boardItem in boardItems) {
                    if (boardItem.itemType == "unsplash") {
                        val photoEntity = database.photoDao().getPhotoById(boardItem.itemId)
                        photoEntity?.let {
                            photos.add(PhotoMapper.fromEntity(it))
                        }
                    }
                }
            }
            photos
        }
    }
} 