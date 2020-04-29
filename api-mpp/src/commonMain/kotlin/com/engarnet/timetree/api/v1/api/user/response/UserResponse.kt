package com.engarnet.timetree.api.v1.api.user.response

import com.engarnet.timetree.api.v1.entity.UserEntity
import com.engarnet.timetree.model.type.Uri
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val data: UserEntity
)