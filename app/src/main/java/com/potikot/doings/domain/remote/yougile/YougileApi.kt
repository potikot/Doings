package com.potikot.doings.domain.remote.yougile

import android.util.Log
import com.potikot.doings.domain.remote.util.ApiError
import com.potikot.doings.domain.remote.util.ApiResponse
import com.potikot.doings.domain.remote.yougile.request.ApiRequest
import com.potikot.doings.domain.remote.yougile.request.GetCompanies
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.reflect.typeInfo

class YougileApi(val client: HttpClient) {
    var apiToken = ""

    private var _isInitialized = false
    val isInitialized = _isInitialized

    suspend fun init(login: String, password: String): ApiResponse<Unit> {
        // todo: check credentials and generate api key

        val response = request(GetCompanies(login, password))
        when(response) {
            is ApiResponse.Error -> return response
            is ApiResponse.Success -> {

            }
        }

        apiToken = buildToken("some api key")
        _isInitialized = true
        return ApiResponse.Success(Unit)
    }

    suspend fun init(key: String): ApiResponse<Unit> {
        // todo: check api key

        apiToken = buildToken(key)
        _isInitialized = true
        return ApiResponse.Success(Unit)
    }

    suspend inline fun <reified R> request(request: ApiRequest<R>): ApiResponse<R> {
        return try {
            val response = unsafeRequest(request)
            if (response.status.isSuccess()) {
                ApiResponse.Success(response.body(typeInfo<R>()))
            } else {
                ApiResponse.Error(
                    ApiError.Http(
                        code = response.status.value,
                        body = response.body()
                    )
                )
            }
        } catch (e: Exception) {
            ApiResponse.Error(ApiError.Unknown(e))
        }
    }

    suspend inline fun <reified R> unsafeRequest(request: ApiRequest<R>): HttpResponse {
        val path = request.path()
        return client.request {
            method = request.method()
            url(API_ROOT + path)
            contentType(ContentType.Application.Json)
            if (!request.requiresAuth()) header("Authorization", apiToken)
            if (request.hasBody()) setBody(request)
        }
    }

    private fun buildToken(apiKey: String): String {
        return "Bearer $apiKey"
    }

    companion object {
        const val API_ROOT = "https://yougile.com/api-v2/"
    }
}
