package com.engarnet.timetree.api.v1.api.user.response

import com.engarnet.timetree.api.v1.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val data: UserEntity
)