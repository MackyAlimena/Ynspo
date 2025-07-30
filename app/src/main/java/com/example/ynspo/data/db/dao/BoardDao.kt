package com.example.ynspo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ynspo.data.db.entity.BoardEntity

@Dao
interface BoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(board: BoardEntity): Long

    @Update
    suspend fun updateBoard(board: BoardEntity)

    @Delete
    suspend fun deleteBoard(board: BoardEntity)

    @Query("SELECT * FROM boards ORDER BY creationDate DESC")
    fun getAllBoards(): LiveData<List<BoardEntity>>

    @Query("SELECT * FROM boards ORDER BY creationDate DESC")
    suspend fun getAllBoardsSync(): List<BoardEntity>

    @Query("SELECT * FROM boards WHERE id = :boardId")
    suspend fun getBoardById(boardId: Long): BoardEntity?
}
