package com.example.ynspo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ynspo.data.db.entity.BoardItemEntity

@Dao
interface BoardItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoardItem(boardItem: BoardItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoardItems(boardItems: List<BoardItemEntity>)

    @Update
    suspend fun updateBoardItem(boardItem: BoardItemEntity)

    @Delete
    suspend fun deleteBoardItem(boardItem: BoardItemEntity)

    @Query("SELECT * FROM board_items WHERE boardId = :boardId ORDER BY savedAt DESC")
    fun getBoardItems(boardId: Long): LiveData<List<BoardItemEntity>>

    @Query("SELECT * FROM board_items WHERE boardId = :boardId ORDER BY savedAt DESC")
    suspend fun getBoardItemsSync(boardId: Long): List<BoardItemEntity>

    @Query("SELECT * FROM board_items ORDER BY savedAt DESC")
    fun getAllBoardItems(): LiveData<List<BoardItemEntity>>

    @Query("SELECT * FROM board_items WHERE boardId = :boardId AND itemId = :itemId LIMIT 1")
    suspend fun getBoardItem(boardId: Long, itemId: String): BoardItemEntity?

    @Query("SELECT * FROM board_items WHERE boardId = :boardId AND itemType = :itemType ORDER BY savedAt DESC")
    suspend fun getBoardItemsByType(boardId: Long, itemType: String): List<BoardItemEntity>

    @Query("DELETE FROM board_items WHERE boardId = :boardId")
    suspend fun deleteBoardItems(boardId: Long)

    @Query("DELETE FROM board_items WHERE boardId = :boardId AND itemId = :itemId")
    suspend fun removeItemFromBoard(boardId: Long, itemId: String)

    @Query("DELETE FROM board_items WHERE boardId = :boardId AND itemId = :itemId AND itemType = :itemType")
    suspend fun deleteBoardItem(boardId: Long, itemId: String, itemType: String)

    @Query("SELECT * FROM board_items WHERE itemId = :itemId AND itemType = :itemType")
    suspend fun getBoardItemsByItemId(itemId: String, itemType: String): List<BoardItemEntity>

    @Query("DELETE FROM board_items WHERE itemId = :itemId AND itemType = :itemType")
    suspend fun deleteBoardItemsByItemId(itemId: String, itemType: String)

    @Query("SELECT EXISTS(SELECT 1 FROM board_items WHERE boardId = :boardId AND itemId = :itemId)")
    suspend fun isItemInBoard(boardId: Long, itemId: String): Boolean
} 