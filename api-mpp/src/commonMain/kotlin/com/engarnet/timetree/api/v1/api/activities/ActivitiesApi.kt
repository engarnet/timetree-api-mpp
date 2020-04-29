package com.engarnet.timetree.api.v1.api.activities

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.activities.params.AddActivityParams
import com.engarnet.timetree.api.v1.api.activities.response.ActivityResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json

open class ActivitiesApi(private val apiClient: ApiClient) {
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

class DeferredActivitiesApi(apiClient: ApiClient) : ActivitiesApi(apiClient) {
    fun addActivityDeferred(params: AddActivityParams): Deferred<ActivityResponse> {
        return GlobalScope.async {
            addActivity(params)
        }
    }
}