package com.example.ynspo.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ynspo.data.db.entity.UserHobbyEntity
import com.example.ynspo.data.db.entity.UserProfileEntity

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfileEntity)

    @Update
    suspend fun updateUserProfile(userProfile: UserProfileEntity)

    @Query("SELECT * FROM user_profile ORDER BY id DESC LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Query("SELECT * FROM user_profile ORDER BY id DESC LIMIT 1")
    fun getUserProfileLiveData(): LiveData<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHobby(hobby: UserHobbyEntity)

    @Query("DELETE FROM user_hobbies WHERE userId = :userId")
    suspend fun deleteAllUserHobbies(userId: String)

    @Query("SELECT * FROM user_hobbies WHERE userId = :userId")
    fun getUserHobbies(userId: String): LiveData<List<UserHobbyEntity>>

    @Delete
    suspend fun deleteUserProfile(userProfile: UserProfileEntity)
    
    @Query("UPDATE user_profile SET darkModeEnabled = :darkModeEnabled WHERE id = :id")
    suspend fun updateDarkModePreference(id: Long, darkModeEnabled: Boolean)
}
