package com.engarnet.timetree.platform.api.client

import com.engarnet.timetree.api.client.ApiClient

expect class DefaultApiClient(accessToken: String) : ApiClient {
    val headers: Map<String, String>
    override suspend fun get(
        path: String,
        headers: Map<String, String>,
        params: Map<String, Any>
    ): String

    override suspend fun post(path: String, headers: Map<String, String>, body: String): String
    override suspend fun put(path: String, headers: Map<String, String>, body: String): String
    override suspend fun delete(path: String, headers: Map<String, String>): String
}