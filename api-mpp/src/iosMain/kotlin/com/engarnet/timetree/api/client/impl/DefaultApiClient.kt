package com.engarnet.timetree.api.client.impl

import com.engarnet.timetree.api.client.ApiClient

actual class DefaultApiClient actual constructor(accessToken: String) : ApiClient {
    actual val headers: Map<String, String>
        get() = TODO("Not yet implemented")

    actual override suspend fun get(
        path: String,
        headers: Map<String, String>,
        params: Map<String, Any>
    ): String {
        TODO("Not yet implemented")
    }

    actual override suspend fun post(
        path: String,
        headers: Map<String, String>,
        body: String
    ): String {
        TODO("Not yet implemented")
    }

    actual override suspend fun put(
        path: String,
        headers: Map<String, String>,
        body: String
    ): String {
        TODO("Not yet implemented")
    }

    actual override suspend fun delete(
        path: String,
        headers: Map<String, String>
    ): String {
        TODO("Not yet implemented")
    }
}