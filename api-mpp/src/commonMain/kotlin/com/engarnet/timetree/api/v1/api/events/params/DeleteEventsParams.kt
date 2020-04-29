package com.engarnet.timetree.api.v1.api.events.params

data class DeleteEventsParams(
    val calendarId: String,
    val eventId: String
)