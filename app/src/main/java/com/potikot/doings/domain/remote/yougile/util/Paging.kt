package com.potikot.doings.domain.remote.yougile.util

import kotlinx.serialization.Serializable

@Serializable
data class Paging(
    val count: Int,
    var limit: Int,
    val offset: Int,
    val next: Boolean
)