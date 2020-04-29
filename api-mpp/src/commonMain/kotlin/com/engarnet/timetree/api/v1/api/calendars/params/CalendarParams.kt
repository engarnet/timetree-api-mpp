package com.engarnet.timetree.api.v1.api.calendars.params

import com.engarnet.timetree.api.v1.entity.ext.toParam
import com.engarnet.timetree.model.type.Include

data class CalendarParams(
    val calendarId: String,
    val include: Include.Calendars?
) {
    fun toQueryParams(): Map<String, String> {
        return include?.let {
            mapOf(
                "include" to it.toParam()
            )
        } ?: mapOf()
    }
}