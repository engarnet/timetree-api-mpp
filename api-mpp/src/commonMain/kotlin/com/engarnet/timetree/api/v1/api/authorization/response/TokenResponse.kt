package com.engarnet.timetree.api.v1.api.authorization.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("created_at") val createdAt: Int,
    val scope: String // TODO: 仕様にないレスポンス
)
