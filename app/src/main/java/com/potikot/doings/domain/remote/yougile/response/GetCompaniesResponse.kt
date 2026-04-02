package com.potikot.doings.domain.remote.yougile.response

import com.potikot.doings.domain.remote.yougile.util.Paging
import kotlinx.serialization.Serializable

@Serializable
data class GetCompaniesResponse(
    val paging: Paging,
    val content: List<Company>
) : ApiResponseData

@Serializable
data class Company(
    val id: String,
    val name: String,
    val isAdmin: Boolean
)