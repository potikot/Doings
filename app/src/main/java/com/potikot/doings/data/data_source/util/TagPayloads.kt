package com.potikot.doings.data.data_source.util

import com.potikot.doings.domain.util.PriorityLevel

object TagPayloads {
    data class Custom(
        val value: String
    )
    data class Deadline(
        val start: String?,
        val end: String?
    )
    data class Priority(
        val level: PriorityLevel
    )
}