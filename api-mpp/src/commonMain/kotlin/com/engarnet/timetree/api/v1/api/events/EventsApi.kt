package com.engarnet.timetree.api.v1.api.events

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.events.params.*
import com.engarnet.timetree.api.v1.api.events.response.EventResponse
import com.engarnet.timetree.api.v1.api.events.response.UpcomingEventsResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json

open class EventsApi(private val apiClient: ApiClient) {
    suspend fun events(params: EventsParams): EventResponse {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId + "/events/" + params.eventId
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers,
            params = params.toQueryParams()
        )
        print("response: $response")
        return Json.parse(EventResponse.serializer(), response)
    }

    suspend fun upcomingEvents(params: UpcomingEventsParams): UpcomingEventsResponse {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId + "/upcoming_events"
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers,
            params = params.toQueryParams()
        )
        print("response: $response")
        return Json.parse(UpcomingEventsResponse.serializer(), response)

    }

    suspend fun addEvent(params: AddEventsParams): EventResponse {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId + "/events"
        val response = apiClient.post(
            path = path,
            headers = Config.V1.headers,
            body = params.toJson()
        )
        print("response: $response")
        return Json.parse(EventResponse.serializer(), response)
    }

    suspend fun updateEvent(params: UpdateEventParams): EventResponse {
        val path =
            Config.V1.apiUrl + "/calendars/" + params.addEventsParams.calendarId + "/events/" + params.eventId
        val response = apiClient.put(
            path = path,
            headers = Config.V1.headers,
            body = params.toJson()
        )
        print("response: $response")
        return Json.parse(EventResponse.serializer(), response)
    }

    suspend fun deleteEvent(params: DeleteEventsParams) {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId + "/events/" + params.eventId
        val response = apiClient.delete(
            path = path,
            headers = Config.V1.headers
        )
        print("response: $response")
    }
}

class DeferredEventsApi(apiClient: ApiClient) : EventsApi(apiClient) {
    fun eventsDeferred(params: EventsParams): Deferred<EventResponse> {
        return GlobalScope.async {
            events(params)
        }
    }

    fun upcomingEventsDeferred(params: UpcomingEventsParams): Deferred<UpcomingEventsResponse> {
        return GlobalScope.async {
            upcomingEvents(params)
        }
    }

    fun addEventDeferred(params: AddEventsParams): Deferred<EventResponse> {
        return GlobalScope.async {
            addEvent(params)
        }
    }

    fun updateEventDeferred(params: UpdateEventParams): Deferred<EventResponse> {
        return GlobalScope.async {
            updateEvent(params)
        }
    }

    fun deleteEventDeferred(params: DeleteEventsParams): Deferred<Unit> {
        return GlobalScope.async {
            deleteEvent(params)
        }
    }
}