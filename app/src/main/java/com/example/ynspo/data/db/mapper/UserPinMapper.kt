package com.example.ynspo.data.db.mapper

import com.example.ynspo.data.db.entity.UserPinEntity
import com.example.ynspo.data.model.UserPin
import com.example.ynspo.data.model.UserPinUrls

object UserPinMapper {
    
    fun fromEntity(entity: UserPinEntity): UserPin {
        return UserPin(
            id = entity.id,
            description = entity.description,
            urls = UserPinUrls(
                small = entity.smallUrl,
                regular = entity.regularUrl,
                full = entity.fullUrl
            ),
            userId = entity.userId,
            userName = entity.userName,
            userPhotoUrl = entity.userPhotoUrl,
            hashtags = entity.hashtags.split(",").filter { it.isNotBlank() },
            keywords = entity.keywords.split(",").filter { it.isNotBlank() },
            createdAt = entity.createdAt,
            isUserPin = true
        )
    }
    
    fun toEntity(userPin: UserPin): UserPinEntity {
        return UserPinEntity(
            id = userPin.id,
            description = userPin.description,
            smallUrl = userPin.urls.small,
            regularUrl = userPin.urls.regular,
            fullUrl = userPin.urls.full,
            userId = userPin.userId,
            userName = userPin.userName,
            userPhotoUrl = userPin.userPhotoUrl,
            hashtags = userPin.hashtags.joinToString(","),
            keywords = userPin.keywords.joinToString(","),
            createdAt = userPin.createdAt,
            savedAt = System.currentTimeMillis()
        )
    }
    
    fun fromEntities(entities: List<UserPinEntity>): List<UserPin> {
        return entities.map { fromEntity(it) }
    }
    
    fun toEntities(userPins: List<UserPin>): List<UserPinEntity> {
        return userPins.map { toEntity(it) }
    }
} 