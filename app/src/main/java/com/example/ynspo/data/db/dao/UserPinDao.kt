package com.example.ynspo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ynspo.data.db.entity.UserPinEntity

@Dao
interface UserPinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPin(userPin: UserPinEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPins(userPins: List<UserPinEntity>)

    @Update
    suspend fun updateUserPin(userPin: UserPinEntity)

    @Delete
    suspend fun deleteUserPin(userPin: UserPinEntity)

    @Query("SELECT * FROM user_pins ORDER BY createdAt DESC")
    fun getAllUserPins(): LiveData<List<UserPinEntity>>

    @Query("SELECT * FROM user_pins WHERE id = :pinId")
    suspend fun getUserPinById(pinId: String): UserPinEntity?

    @Query("SELECT * FROM user_pins WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserPinsByUserId(userId: String): LiveData<List<UserPinEntity>>

    // Búsqueda por descripción, hashtags o keywords
    @Query("SELECT * FROM user_pins WHERE description LIKE '%' || :query || '%' OR hashtags LIKE '%' || :query || '%' OR keywords LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    suspend fun searchUserPins(query: String): List<UserPinEntity>

    // Obtener pines recientes
    @Query("SELECT * FROM user_pins ORDER BY createdAt DESC LIMIT :limit")
    suspend fun getRecentUserPins(limit: Int = 20): List<UserPinEntity>

    // Eliminar pines de un usuario específico
    @Query("DELETE FROM user_pins WHERE userId = :userId")
    suspend fun deleteUserPinsByUserId(userId: String)

    // Eliminar un pin específico por ID
    @Query("DELETE FROM user_pins WHERE id = :pinId")
    suspend fun deleteUserPinById(pinId: String)

    // Eliminar todos los UserPins
    @Query("DELETE FROM user_pins")
    suspend fun deleteAllUserPins()
} 