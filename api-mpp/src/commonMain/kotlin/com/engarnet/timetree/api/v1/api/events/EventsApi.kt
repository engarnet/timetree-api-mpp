package com.engarnet.timetree.api.v1.api.events

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.events.params.*
import com.engarnet.timetree.api.v1.api.events.response.EventResponse
import com.engarnet.timetree.api.v1.api.events.response.UpcomingEventsResponse
import kotlinx.serialization.json.Json

open class EventsApi(private val apiClient: ApiClient) {
    suspend fun event(params: EventParams): EventResponse {
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