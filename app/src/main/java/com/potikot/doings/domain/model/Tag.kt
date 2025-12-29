package com.potikot.doings.domain.model

import com.potikot.doings.domain.util.PriorityLevel
import java.time.Instant
import java.time.LocalDateTime

sealed class Tag() {
    abstract val common: CommonTagData

    data class Custom(
        override val common: CommonTagData,
        val value: String = ""
    ) : Tag()

    data class Deadline(
        override val common: CommonTagData,
        val start: LocalDateTime? = null,
        val end: LocalDateTime? = null
    ) : Tag()

    data class Priority(
        override val common: CommonTagData,
        val level: PriorityLevel
    ) : Tag()
}

@JvmInline
value class TagId(override val value: Long): ID

data class CommonTagData(
    val id: TagId = TagId(0),
    val externalId: String? = null,
    val position: Int,
    val createdAt: Instant = Instant.now()
)