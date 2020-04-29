package com.engarnet.timetree.api.v1.api.calendars.response

import com.engarnet.timetree.api.v1.entity.CalendarEntity
import com.engarnet.timetree.api.v1.entity.IncludedEntity
import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponse(
    val data: CalendarEntity,
    val included: List<IncludedEntity>
)