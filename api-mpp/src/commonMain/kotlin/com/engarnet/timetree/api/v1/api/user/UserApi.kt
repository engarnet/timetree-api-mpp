package com.engarnet.timetree.api.v1.api.user

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.user.response.UserResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json

open class UserApi(private val apiClient: ApiClient) {
    suspend fun user(): UserResponse {
        val path = Config.V1.apiUrl + "/user"
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers
        )

        return Json.parse(UserResponse.serializer(), response)
    }
}
