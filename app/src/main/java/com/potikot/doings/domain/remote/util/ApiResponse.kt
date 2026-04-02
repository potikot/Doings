package com.potikot.doings.domain.remote.util

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T?) : ApiResponse<T>
    data class Error(val error: ApiError) : ApiResponse<Nothing>
}