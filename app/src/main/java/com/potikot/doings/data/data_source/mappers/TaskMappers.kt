package com.potikot.doings.data.data_source.mappers

import com.potikot.doings.data.data_source.TaskWithTags
import com.potikot.doings.data.data_source.entity.TaskEntity
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Task
import com.potikot.doings.domain.model.TaskId
import java.time.Instant

fun TaskWithTags.toDomain(): Task {
    return Task(
        id = TaskId(this.task.id),
        externalId = this.task.externalId,
        name = this.task.title,
        description = this.task.description,
        isCompleted = this.task.isCompleted,
        position = this.task.position,
        tags = this.tags.map { it.toDomain() },
        createdAt = Instant.ofEpochMilli(this.task.createdAt)
    )
}

fun Task.toEntity(parentId: ColumnId): TaskEntity {
    return TaskEntity(
        id = this.id.value,
        externalId = this.externalId,
        columnId = parentId.value,
        title = this.name,
        description = this.description,
        isCompleted = this.isCompleted,
        position = this.position,
        createdAt = this.createdAt.toEpochMilli()
    )
}

fun Task.toEntityRelation(parentId: ColumnId): TaskWithTags {
    return TaskWithTags(
        task = this.toEntity(parentId),
        tags = this.tags.map { it.toEntity(this.id) }
    )
}