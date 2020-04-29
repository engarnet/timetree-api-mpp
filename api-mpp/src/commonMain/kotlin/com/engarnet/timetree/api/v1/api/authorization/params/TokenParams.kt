package com.engarnet.timetree.api.v1.api.authorization.params

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class TokenParams(
    val clientId: String,
    val clientSecret: String,
    val redirectUrl: String,
    val code: String,
    val grantType: String = "authorization_code",
    val codeVerifier: String?
) {
    fun toJson(): String {
        return Params(
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUrl = redirectUrl,
            code = code,
            grantType = grantType,
            codeVerifier = codeVerifier
        ).let {
            Json.stringify(Params.serializer(), it)
        }
    }

    @Serializable
    private data class Params(
        @SerialName("client_id") val clientId: String,
        @SerialName("client_secret") val clientSecret: String,
        @SerialName("redirect_uri") val redirectUrl: String,
        val code: String,
        @SerialName("grant_type") val grantType: String,
        @SerialName("code_verifier") val codeVerifier: String?
    )
}