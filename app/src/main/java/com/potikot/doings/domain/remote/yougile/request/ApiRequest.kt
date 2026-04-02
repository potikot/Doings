package com.potikot.doings.domain.remote.yougile.request

import io.ktor.http.HttpMethod
import kotlinx.serialization.Serializable

@Serializable
sealed interface ApiRequest<T> {
    fun method(): HttpMethod
    fun path(): String
    fun hasBody(): Boolean = true
    fun requiresAuth(): Boolean = !path().startsWith("auth")
}