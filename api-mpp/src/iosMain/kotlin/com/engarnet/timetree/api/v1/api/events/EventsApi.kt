package com.engarnet.timetree.api.v1.api.events

import com.engarnet.timetree.api.v1.api.events.params.*
import com.engarnet.timetree.api.v1.api.events.response.EventResponse
import com.engarnet.timetree.api.v1.api.events.response.UpcomingEventsResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun EventsApi.events(params: EventsParams): Deferred<EventResponse> {
    return GlobalScope.async {
        events(params)
    }
}

fun EventsApi.upcomingEvents(params: UpcomingEventsParams): Deferred<UpcomingEventsResponse> {
    return GlobalScope.async {
        upcomingEvents(params)
    }
}

fun EventsApi.addEvent(params: AddEventsParams): Deferred<EventResponse> {
    return GlobalScope.async {
        addEvent(params)
    }
}

fun EventsApi.updateEvent(params: UpdateEventParams): Deferred<EventResponse> {
    return GlobalScope.async {
        updateEvent(params)
    }
}

fun EventsApi.deleteEvent(params: DeleteEventsParams): Deferred<Unit> {
    return GlobalScope.async {
        deleteEvent(params)
    }
}