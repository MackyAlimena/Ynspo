package com.example.ynspo.data.db.mapper

import com.example.ynspo.data.db.entity.BoardEntity
import com.example.ynspo.data.model.Board

object BoardMapper {

    fun toEntity(board: Board): BoardEntity {
        return BoardEntity(
            id = if (board.id > 0) board.id else 0L,
            name = board.name
        )
    }

    fun fromEntity(entity: BoardEntity): Board {
        return Board(
            id = entity.id,
            name = entity.name
        )
    }

    fun fromEntities(entities: List<BoardEntity>): List<Board> {
        return entities.map { fromEntity(it) }
    }

    fun toEntities(boards: List<Board>): List<BoardEntity> {
        return boards.map { toEntity(it) }
    }
}
