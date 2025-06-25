package com.example.ynspo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.db.entity.BoardPhotosCrossRef
import com.example.ynspo.data.db.mapper.BoardMapper
import com.example.ynspo.data.db.mapper.PhotoMapper
import com.example.ynspo.data.model.Board
import com.example.ynspo.data.model.UnsplashPhoto
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BoardsRoomRepository @Inject constructor(
    private val database: YnspoDatabase
) {
    // Obtener todos los tableros con sus fotos
    val boards: LiveData<List<Board>> = database.boardDao().getAllBoardsWithPhotos().map { boardsWithPhotos ->
        BoardMapper.fromBoardsWithPhotos(boardsWithPhotos)
    }

    // Obtener las fotos para un tablero específico
    fun getBoardPhotos(boardId: Long): LiveData<List<UnsplashPhoto>> {
        return database.boardPhotoDao().getPhotosForBoard(boardId).map { entities ->
            PhotoMapper.fromEntities(entities)
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
    }

    // Agregar una foto a un tablero
    suspend fun addPhotoToBoard(boardId: Long, photo: UnsplashPhoto) {
        // Primero aseguramos que la foto exista en la base de datos
        val photoEntity = PhotoMapper.toEntity(photo)
        database.photoDao().insertPhoto(photoEntity)

        // Luego creamos la relación entre el tablero y la foto
        val crossRef = BoardPhotosCrossRef(boardId = boardId, photoId = photo.id)
        database.boardPhotoDao().addPhotoToBoard(crossRef)
    }

    // Eliminar una foto de un tablero
    suspend fun removePhotoFromBoard(boardId: Long, photoId: String) {
        database.boardPhotoDao().removePhotoFromBoard(boardId, photoId)
    }

    // Verificar si una foto está en un tablero
    suspend fun isPhotoInBoard(boardId: Long, photoId: String): Boolean {
        return database.boardPhotoDao().isPhotoInBoard(boardId, photoId)
    }

    // Obtener un tablero por su ID con sus fotos
    suspend fun getBoardById(boardId: Long): Board? {
        return database.boardDao().getBoardWithPhotos(boardId)?.let {
            BoardMapper.fromBoardWithPhotos(it)
        }
    }
}
