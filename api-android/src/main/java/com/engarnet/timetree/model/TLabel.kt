package com.engarnet.timetree.model

import com.engarnet.timetree.api.v1.api.calendars.response.CalendarLabelsResponse

data class TLabel(
    val id: String,
    val name: String,
    val color: Int
)

internal fun CalendarLabelsResponse.toModel(): List<TLabel> {
    return data.map {
        TLabel(
            id = it.id,
            name = it.attributes.name,
            color = it.attributes.color
        )
    }
}