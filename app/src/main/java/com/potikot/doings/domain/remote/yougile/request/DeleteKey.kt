package com.potikot.doings.domain.remote.yougile.request

import com.potikot.doings.domain.remote.yougile.response.EmptyResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

@Serializable
data class DeleteKey(
    val key: String
) : ApiRequest<EmptyResponse> {
    override fun method() = HttpMethod.Companion.Delete
    override fun path() = "auth/keys/$key"
    override fun hasBody() = false
    override fun requiresAuth() = false
}