package com.example.ynspo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ynspo.data.db.entity.FirebaseUserEntity

@Dao
interface FirebaseUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: FirebaseUserEntity)

    @Update
    suspend fun updateUser(user: FirebaseUserEntity)

    @Query("SELECT * FROM firebase_user WHERE uid = :uid LIMIT 1")
    suspend fun getUserById(uid: String): FirebaseUserEntity?

    @Query("SELECT * FROM firebase_user ORDER BY lastLoginTime DESC LIMIT 1")
    fun getLastLoggedUser(): LiveData<FirebaseUserEntity?>

    @Query("SELECT * FROM firebase_user ORDER BY lastLoginTime DESC LIMIT 1")
    suspend fun getLastLoggedUserDirect(): FirebaseUserEntity?

    @Query("DELETE FROM firebase_user WHERE uid = :uid")
    suspend fun deleteUser(uid: String)

    @Query("DELETE FROM firebase_user")
    suspend fun deleteAllUsers()
} 