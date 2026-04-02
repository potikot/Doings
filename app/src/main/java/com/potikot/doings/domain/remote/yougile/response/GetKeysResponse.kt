package com.potikot.doings.domain.remote.yougile.response

import kotlinx.serialization.Serializable

@Serializable
data class GetKeysResponse(
    val key: String,
    val companyId: String,
    val timestamp: Long,
    val deleted: Boolean? = null
) : ApiResponseData