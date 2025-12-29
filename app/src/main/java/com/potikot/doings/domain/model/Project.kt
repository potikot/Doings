package com.potikot.doings.domain.model

import java.time.Instant

data class Project(
    val id: ProjectId = ProjectId(0),
    val externalId: String? = null,
    val name: String,
    val selectedBoardId: BoardId? = null,
    val boards: List<Board> = emptyList(),
    val createdAt: Instant = Instant.now()
)

@JvmInline
value class ProjectId(override val value: Long): ID