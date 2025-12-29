package com.potikot.doings.data.data_source.mappers

import com.potikot.doings.data.data_source.ProjectWithBoards
import com.potikot.doings.data.data_source.entity.ProjectEntity
import com.potikot.doings.domain.model.AccountId
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.ProjectId
import java.time.Instant

fun ProjectWithBoards.toDomain(): Project {
    return Project(
        id = ProjectId(this.project.id),
        externalId = this.project.externalId,
        name = this.project.name,
        selectedBoardId = this.project.selectedBoardId?.let { BoardId(it) },
        boards = this.boards.map { it.toDomain() },
        createdAt = Instant.ofEpochMilli(this.project.createdAt)
    )
}

fun Project.toEntity(parentId: AccountId): ProjectEntity {
    return ProjectEntity(
        id = this.id.value,
        externalId = this.externalId,
        accountId = parentId.value,
        name = this.name,
        selectedBoardId = this.selectedBoardId?.value,
        createdAt = this.createdAt.toEpochMilli()
    )
}

fun Project.toEntityRelation(parentId: AccountId): ProjectWithBoards {
    return ProjectWithBoards(
        project = this.toEntity(parentId),
        boards = this.boards.map { it.toEntityRelation(this.id) }
    )
}