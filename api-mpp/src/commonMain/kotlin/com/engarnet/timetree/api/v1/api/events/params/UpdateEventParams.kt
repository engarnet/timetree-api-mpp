package com.engarnet.timetree.api.v1.api.events.params

data class UpdateEventParams(
    val eventId: String,
    val addEventsParams: AddEventsParams
) {
    fun toJson(): String = addEventsParams.toJson()
}