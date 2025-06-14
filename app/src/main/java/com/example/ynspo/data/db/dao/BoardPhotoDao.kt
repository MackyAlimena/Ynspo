package com.example.ynspo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.ynspo.data.db.entity.BoardPhotosCrossRef
import com.example.ynspo.data.db.entity.PhotoEntity

@Dao
interface BoardPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhotoToBoard(crossRef: BoardPhotosCrossRef)

    @Query("DELETE FROM board_photo_cross_ref WHERE boardId = :boardId AND photoId = :photoId")
    suspend fun removePhotoFromBoard(boardId: Long, photoId: String)

    @Query("SELECT * FROM photos INNER JOIN board_photo_cross_ref ON photos.id = board_photo_cross_ref.photoId WHERE board_photo_cross_ref.boardId = :boardId ORDER BY board_photo_cross_ref.addedAt DESC")
    fun getPhotosForBoard(boardId: Long): LiveData<List<PhotoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM board_photo_cross_ref WHERE boardId = :boardId AND photoId = :photoId)")
    suspend fun isPhotoInBoard(boardId: Long, photoId: String): Boolean
}
