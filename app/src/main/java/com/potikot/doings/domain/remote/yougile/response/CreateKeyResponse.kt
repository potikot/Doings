package com.potikot.doings.domain.remote.yougile.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateKeyResponse(
    val key: String
) : ApiResponseData