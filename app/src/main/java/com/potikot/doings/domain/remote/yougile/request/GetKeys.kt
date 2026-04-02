package com.potikot.doings.domain.remote.yougile.request

import com.potikot.doings.domain.remote.yougile.response.GetKeysResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

@Serializable
data class GetKeys(
    val login: String,
    val password: String,
    val companyId: String
) : ApiRequest<List<GetKeysResponse>> {
    override fun method() = HttpMethod.Companion.Post
    override fun path() = "auth/keys/get"
    override fun requiresAuth() = false
}