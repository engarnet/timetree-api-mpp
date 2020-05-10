package com.engarnet.timetree.model

import com.engarnet.timetree.api.v1.api.authorization.response.TokenResponse
import java.util.*

data class TAccessToken(
    val accessToken: String,
    val tokenType: String,
    val scope: String,
    val createdAt: Date
)

internal fun TokenResponse.toModel(): TAccessToken {
    val response = this
    return TAccessToken(
        accessToken = response.accessToken,
        tokenType = response.tokenType,
        scope = response.scope,
        createdAt = Date().apply { time = response.createdAt.toLong() }
    )
}