package com.potikot.doings.domain.model

import java.time.Instant

data class Column(
    val id: ColumnId = ColumnId(0),
    val externalId: String? = null,
    val name: String,
    val position: Int,
    val tasks: List<Task> = emptyList(),
    val createdAt: Instant = Instant.now()
)

@JvmInline
value class ColumnId(override val value: Long): ID