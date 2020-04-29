package com.engarnet.timetree.api.v1.api.calendars.response

import com.engarnet.timetree.api.v1.entity.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class CalendarMembersResponse(
    val data: List<UserEntity>
)