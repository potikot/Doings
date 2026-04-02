package com.potikot.doings.domain.remote.util

import io.ktor.utils.io.errors.IOException

sealed interface ApiError {
    data class Http(val code: Int, val body: String?) : ApiError
    data class Network(val cause: IOException) : ApiError
    data class Serialization(val cause: Throwable) : ApiError
    data class Unknown(val cause: Throwable) : ApiError
}