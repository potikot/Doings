package com.potikot.doings.domain.model

import java.time.Instant

data class Task(
    val id: TaskId = TaskId(0),
    val externalId: String? = null,
    val name: String,
    val description: String?,
    val isCompleted: Boolean,
    val position: Int,
    val tags: List<Tag> = emptyList(),
    val createdAt: Instant = Instant.now()
)

@JvmInline
value class TaskId(override val value: Long): ID