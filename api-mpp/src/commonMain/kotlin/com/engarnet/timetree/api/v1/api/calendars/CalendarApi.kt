package com.engarnet.timetree.api.v1.api.calendars

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarLabelsParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarMembersParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarsParams
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarLabelsResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarMembersResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarsResponse
import kotlinx.serialization.json.Json

open class CalendarApi(private val apiClient: ApiClient) {
    suspend fun calendars(params: CalendarsParams): CalendarsResponse {
        val path = Config.V1.apiUrl + "/calendars"
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers,
            params = params.toQueryParams()
        )
        print("response: $response")
        return Json.parse(CalendarsResponse.serializer(), response)
    }

    suspend fun calendar(params: CalendarParams): CalendarResponse {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers,
            params = params.toQueryParams()
        )
        print("response: $response")
        return Json.parse(CalendarResponse.serializer(), response)
    }

    suspend fun calendarLabels(params: CalendarLabelsParams): CalendarLabelsResponse {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId + "/labels"
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers
        )
        print("response: $response")
        return Json.parse(CalendarLabelsResponse.serializer(), response)
    }

    suspend fun calendarMembers(params: CalendarMembersParams): CalendarMembersResponse {
        val path = Config.V1.apiUrl + "/calendars/" + params.calendarId + "/members"
        val response = apiClient.get(
            path = path,
            headers = Config.V1.headers
        )
        print("response: $response")
        return Json.parse(CalendarMembersResponse.serializer(), response)
    }
}