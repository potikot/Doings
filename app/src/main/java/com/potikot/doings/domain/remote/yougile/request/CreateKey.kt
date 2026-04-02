package com.potikot.doings.domain.remote.yougile.request

import com.potikot.doings.domain.remote.yougile.response.CreateKeyResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

@Serializable
data class CreateKey(
    val login: String,
    val password: String,
    val companyId: String
) : ApiRequest<CreateKeyResponse> {
    override fun method() = HttpMethod.Companion.Post
    override fun path() = "auth/keys"
    override fun requiresAuth() = false
}