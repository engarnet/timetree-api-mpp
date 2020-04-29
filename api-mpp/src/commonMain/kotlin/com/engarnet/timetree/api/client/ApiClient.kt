package com.engarnet.timetree.api.client

import com.engarnet.timetree.api.v1.entity.ErrorEntity
import com.engarnet.timetree.exception.HttpRequestException
import kotlinx.serialization.json.Json

interface ApiClient {
    suspend fun get(
        path: String,
        headers: Map<String, String>,
        params: Map<String, Any> = mapOf()
    ): String

    suspend fun post(path: String, headers: Map<String, String>, body: String): String
    suspend fun put(path: String, headers: Map<String, String>, body: String): String
    suspend fun delete(path: String, headers: Map<String, String>): String

    fun handleError(responseCode: Int, body: String?) {
        // 正常終了
        if (responseCode in 200..299) return

        body?.let {
            val error = Json.parse(ErrorEntity.serializer(), it)
            throw HttpRequestException(responseCode, error)
        } ?: throw throw IllegalStateException("error body is null")
    }
}