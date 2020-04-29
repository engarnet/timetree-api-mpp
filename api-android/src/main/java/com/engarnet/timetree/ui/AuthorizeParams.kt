package com.engarnet.timetree.ui

import android.util.Base64
import com.engarnet.timetree.api.v1.api.authorization.params.TokenParams
import kotlinx.serialization.Serializable
import java.security.MessageDigest

@Serializable
data class AuthorizeParams(
    val clientId: String,
    val clientSecret: String,
    val redirectUrl: String,
    val responseType: String = "code",
    val state: String,
    val codeVerifier: String?
) {
    fun toQueryParams(): String {
        val c = codeVerifier
        val s = codeVerifier?.sha256
        return listOfNotNull(
            "client_id" to clientId,
            "redirect_uri" to redirectUrl,
            "response_type" to responseType,
            "state" to state,
            if (codeVerifier != null) "code_challenge" to codeVerifier.sha256 else null,
            if (codeVerifier != null) "code_challenge_method" to "S256" else null
        ).joinToString("&") { "${it.first}=${it.second}" }
    }
}

fun AuthorizeParams.toTokenParam(code: String): TokenParams {
    return TokenParams(
        clientId = clientId,
        clientSecret = clientSecret,
        redirectUrl = redirectUrl,
        code = code,
        codeVerifier = codeVerifier
    )
}

private val String.sha256: String
    get() {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(toByteArray())
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
    }
