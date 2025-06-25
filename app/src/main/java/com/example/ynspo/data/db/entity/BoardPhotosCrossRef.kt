package com.example.ynspo.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "board_photo_cross_ref",
    primaryKeys = ["boardId", "photoId"],
    foreignKeys = [
        ForeignKey(
            entity = BoardEntity::class,
            parentColumns = ["id"],
            childColumns = ["boardId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PhotoEntity::class,
            parentColumns = ["id"],
            childColumns = ["photoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BoardPhotosCrossRef(
    val boardId: Long,
    val photoId: String,
    val addedAt: Long = System.currentTimeMillis()
)
