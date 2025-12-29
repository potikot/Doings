package com.potikot.doings.domain.model

import java.time.Instant

data class Board(
    val id: BoardId = BoardId(0),
    val externalId: String? = null,
    val name: String,
    val position: Int,
    val selectedColumnId: ColumnId? = null,
    val columns: List<Column> = emptyList(),
    val createdAt: Instant = Instant.now()
)

@JvmInline
value class BoardId(override val value: Long): ID