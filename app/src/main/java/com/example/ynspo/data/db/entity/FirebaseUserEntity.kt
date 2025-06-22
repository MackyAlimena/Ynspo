package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "firebase_user")
data class FirebaseUserEntity(
    @PrimaryKey
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isEmailVerified: Boolean = false,
    val lastLoginTime: Long = System.currentTimeMillis(),
    val creationTime: Long = System.currentTimeMillis()
) 