package com.engarnet.timetree.api.v1.api.activity

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.activity.params.AddActivityParams
import com.engarnet.timetree.api.v1.api.activity.response.ActivityResponse
import kotlinx.serialization.json.Json

open class ActivityApi(private val apiClient: ApiClient) {
    suspend fun addActivity(params: AddActivityParams): ActivityResponse {
        val path =
            Config.V1.apiUrl + "/calendars/" + params.calendarId + "/events/" + params.eventId + "/activities"
        val response = apiClient.post(
            path = path,
            headers = Config.V1.headers,
            body = params.toJson()
        )
        print("response: $response")
        return Json.parse(ActivityResponse.serializer(), response)
    }
}
