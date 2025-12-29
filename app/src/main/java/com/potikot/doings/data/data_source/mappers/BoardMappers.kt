package com.potikot.doings.data.data_source.mappers

import com.potikot.doings.data.data_source.BoardWithColumns
import com.potikot.doings.data.data_source.entity.BoardEntity
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.ProjectId
import java.time.Instant

fun BoardWithColumns.toDomain(): Board {
    return Board(
        id = BoardId(this.board.id),
        externalId = this.board.externalId,
        name = this.board.name,
        position = this.board.position,
        selectedColumnId = this.board.selectedColumnId?.let { ColumnId(it) },
        columns = this.columns.map { it.toDomain() },
        createdAt = Instant.ofEpochMilli(this.board.createdAt)
    )
}

fun Board.toEntity(parentId: ProjectId): BoardEntity {
    return BoardEntity(
        id = this.id.value,
        externalId = this.externalId,
        projectId = parentId.value,
        name = this.name,
        position = this.position,
        selectedColumnId = this.selectedColumnId?.value,
        createdAt = this.createdAt.toEpochMilli()
    )
}

fun Board.toEntityRelation(parentId: ProjectId): BoardWithColumns {
    return BoardWithColumns(
        board = this.toEntity(parentId),
        columns = this.columns.map { it.toEntityRelation(this.id) }
    )
}