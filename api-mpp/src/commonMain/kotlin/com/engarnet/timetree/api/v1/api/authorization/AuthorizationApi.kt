package com.engarnet.timetree.api.v1.api.authorization

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.authorization.params.TokenParams
import com.engarnet.timetree.api.v1.api.authorization.response.TokenResponse
import kotlinx.serialization.json.Json

open class AuthorizationApi(private val apiClient: ApiClient) {
    suspend fun token(params: TokenParams): TokenResponse {
        val path = Config.V1.tokenUrl
        val response = apiClient.post(
            path = path,
            headers = Config.V1.headers,
            body = params.toJson()
        )
        print("response: $response")
        return Json.parse(TokenResponse.serializer(), response)
    }
}
