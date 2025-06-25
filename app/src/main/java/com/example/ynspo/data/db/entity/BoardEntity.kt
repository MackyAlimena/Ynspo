package com.example.ynspo.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "boards")
data class BoardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val creationDate: Long = System.currentTimeMillis()
)

data class BoardWithPhotos(
    @Embedded val board: BoardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = BoardPhotosCrossRef::class,
            parentColumn = "boardId",
            entityColumn = "photoId"
        )
    )
    val photos: List<PhotoEntity>
)
