package com.engarnet.timetree.api.v1.api.events.params

import com.engarnet.timetree.api.v1.entity.ext.toParam
import com.engarnet.timetree.type.Include
import com.engarnet.timetree.type.TimeZone

data class UpcomingEventsParams(
    val calendarId: String,
    val timeZone: TimeZone?,
    val days: Int?,
    val include: Include.Events?
) {
    fun toQueryParams(): Map<String, String> {
        return listOfNotNull(
            if (timeZone != null) "timezone" to timeZone.toString() else null,
            if (days != null) "days" to days.toString() else null,
            if (include != null) "include" to include.toParam() else null
        ).map {
            it.first to it.second
        }.toMap()
    }
}