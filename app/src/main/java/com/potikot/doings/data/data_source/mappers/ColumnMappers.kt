package com.potikot.doings.data.data_source.mappers

import com.potikot.doings.data.data_source.ColumnWithTasks
import com.potikot.doings.data.data_source.entity.ColumnEntity
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Column
import com.potikot.doings.domain.model.ColumnId
import java.time.Instant

fun ColumnWithTasks.toDomain(): Column {
    return Column(
        id = ColumnId(this.column.id),
        externalId = this.column.externalId,
        name = this.column.name,
        position = this.column.position,
        tasks = this.tasks.map { it.toDomain() },
        createdAt = Instant.ofEpochMilli(this.column.createdAt)
    )
}

fun Column.toEntity(parentId: BoardId): ColumnEntity {
    return ColumnEntity(
        id = this.id.value,
        externalId = this.externalId,
        boardId = parentId.value,
        name = this.name,
        position = this.position,
        createdAt = this.createdAt.toEpochMilli()
    )
}

fun Column.toEntityRelation(parentId: BoardId): ColumnWithTasks {
    return ColumnWithTasks(
        column = this.toEntity(parentId),
        tasks = this.tasks.map { it.toEntityRelation(this.id) }
    )
}