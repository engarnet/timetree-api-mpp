package com.engarnet.timetree.api.v1.api.activity.response

import com.engarnet.timetree.api.v1.entity.ActivityEntity
import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    val data: ActivityEntity
)
