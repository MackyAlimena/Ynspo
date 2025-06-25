package com.example.ynspo.data.db.mapper

import com.example.ynspo.data.db.entity.BoardEntity
import com.example.ynspo.data.db.entity.BoardWithPhotos
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

    fun fromBoardWithPhotos(boardWithPhotos: BoardWithPhotos): Board {
        return Board(
            id = boardWithPhotos.board.id,
            name = boardWithPhotos.board.name,
            photos = PhotoMapper.fromEntities(boardWithPhotos.photos).toMutableList()
        )
    }

    fun fromBoardsWithPhotos(boardsWithPhotos: List<BoardWithPhotos>): List<Board> {
        return boardsWithPhotos.map { fromBoardWithPhotos(it) }
    }

    fun fromEntities(entities: List<BoardEntity>): List<Board> {
        return entities.map { fromEntity(it) }
    }

    fun toEntities(boards: List<Board>): List<BoardEntity> {
        return boards.map { toEntity(it) }
    }
}
