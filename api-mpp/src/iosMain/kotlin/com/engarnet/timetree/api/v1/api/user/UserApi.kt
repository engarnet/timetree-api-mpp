package com.engarnet.timetree.api.v1.api.user

import com.engarnet.timetree.api.v1.api.user.response.UserResponse
import kotlinx.coroutines.*

fun UserApi.user(): Deferred<UserResponse> {
    return GlobalScope.async {
        user()
    }
}