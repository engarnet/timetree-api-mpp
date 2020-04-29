package com.engarnet.timetree.api.v1.api.calendars.response

import com.engarnet.timetree.api.v1.entity.LabelEntity
import kotlinx.serialization.Serializable

@Serializable
data class CalendarLabelsResponse(val data: List<LabelEntity>)