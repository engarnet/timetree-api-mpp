package com.engarnet.timetree.api.v1.api.events.params

import com.engarnet.timetree.api.v1.entity.ext.toParam
import com.engarnet.timetree.type.Include

data class EventParams(
    val calendarId: String,
    val eventId: String,
    val include: Include.Events?
) {
    fun toQueryParams(): Map<String, String> {
        return include?.let {
            mapOf(
                "include" to it.toParam()
            )
        } ?: mapOf()
    }
}
