package com.potikot.doings.domain.remote.yougile.request

import com.potikot.doings.domain.remote.yougile.response.GetCompaniesResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

@Serializable
data class GetCompanies(
    val login: String,
    val password: String,
    val name: String? = null
) : ApiRequest<GetCompaniesResponse> {
    override fun method() = HttpMethod.Post
    override fun path() = "auth/companies"
    override fun requiresAuth() = false
}