package com.example.ynspo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.ynspo.data.db.YnspoDatabase
import com.example.ynspo.data.db.mapper.UserProfileMapper
import com.example.ynspo.ui.profile.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserProfileRepository @Inject constructor(
    private val database: YnspoDatabase
) {
    private val userId = "user_profile"

    // Obtener el perfil de usuario
    val userProfile: LiveData<UserProfile?> = database.userProfileDao().getUserProfileLiveData().map { profileEntity ->
        profileEntity?.let { profile ->
            val hobbies = database.userProfileDao().getUserHobbies(userId).value ?: emptyList()
            UserProfileMapper.fromEntity(profile, hobbies)
        }
    }

    // Guardar o actualizar el perfil de usuario
    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        // Guardar la información básica del perfil
        val profileEntity = UserProfileMapper.toEntity(profile)
        database.userProfileDao().insertUserProfile(profileEntity)

        // Borrar los hobbies anteriores y guardar los nuevos
        database.userProfileDao().deleteAllUserHobbies(userId)
        val hobbyEntities = UserProfileMapper.toHobbyEntities(profile)
        hobbyEntities.forEach { hobby ->
            database.userProfileDao().insertHobby(hobby)
        }
    }
}
