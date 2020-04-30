package com.engarnet.timetree.api.v1.api.authorization

import com.engarnet.timetree.api.v1.api.authorization.params.TokenParams
import com.engarnet.timetree.api.v1.api.authorization.response.TokenResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun AuthorizationApi.token(params: TokenParams): Deferred<TokenResponse> {
    return GlobalScope.async {
        token(params)
    }
}
