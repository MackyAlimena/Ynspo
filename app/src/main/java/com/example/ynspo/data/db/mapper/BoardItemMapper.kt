package com.example.ynspo.data.db.mapper

import com.example.ynspo.data.db.entity.BoardItemEntity
import com.example.ynspo.data.model.InspirationItem

object BoardItemMapper {
    
    fun toEntity(boardId: Long, inspirationItem: InspirationItem): BoardItemEntity {
        return BoardItemEntity(
            boardId = boardId,
            itemId = inspirationItem.id,
            itemType = if (inspirationItem.isUserPin) "user_pin" else "unsplash",
            savedAt = System.currentTimeMillis()
        )
    }
    
    fun fromEntity(entity: BoardItemEntity): Pair<Long, String> {
        return Pair(entity.boardId, entity.itemId)
    }
} 