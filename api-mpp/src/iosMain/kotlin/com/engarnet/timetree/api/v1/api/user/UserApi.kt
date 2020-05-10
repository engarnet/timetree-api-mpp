package com.engarnet.timetree.api.v1.api.user

import com.engarnet.timetree.api.v1.api.user.response.UserResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun UserApi.user(): Deferred<UserResponse> {
    return GlobalScope.async {
        user()
    }
}