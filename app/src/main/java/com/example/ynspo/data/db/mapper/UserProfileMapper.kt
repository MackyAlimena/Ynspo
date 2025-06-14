package com.example.ynspo.data.db.mapper

import com.example.ynspo.data.db.entity.UserHobbyEntity
import com.example.ynspo.data.db.entity.UserProfileEntity
import com.example.ynspo.ui.profile.UserProfile


object UserProfileMapper {

    fun toEntity(profile: UserProfile): UserProfileEntity {
        return UserProfileEntity(
            name = profile.name,
            photoUrl = profile.photoUrl,
            bio = profile.bio
        )
    }

    fun fromEntity(entity: UserProfileEntity, hobbies: List<UserHobbyEntity>): UserProfile {
        return UserProfile(
            name = entity.name,
            photoUrl = entity.photoUrl,
            bio = entity.bio,
            hobbies = hobbies.map { it.hobby }
        )
    }

    fun toHobbyEntities(profile: UserProfile, userId: String = "user_profile"): List<UserHobbyEntity> {
        return profile.hobbies.map { hobby ->
            UserHobbyEntity(
                userId = userId,
                hobby = hobby
            )
        }
    }
}
