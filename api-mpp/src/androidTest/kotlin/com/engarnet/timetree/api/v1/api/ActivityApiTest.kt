package com.engarnet.timetree.api.v1.api

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.platform.api.client.DefaultApiClient
import com.engarnet.timetree.api.v1.api.activity.ActivityApi
import com.engarnet.timetree.api.v1.api.activity.params.AddActivityParams
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ActivityApiTest {
    private lateinit var apiClient: ApiClient

    private val accessToken = ""
    private val calendarId = ""
    private val eventId = ""

    @Before
    fun setup() {
        apiClient = DefaultApiClient(
            accessToken
        )
    }

    @Test
    fun test_calendars() {
        runBlocking {
            val api =
                ActivityApi(apiClient)

            AddActivityParams(
                calendarId = calendarId,
                eventId = eventId,
                content = "test activity"
            ).runCatching {
                api.addActivity(this)
            }.onSuccess {
                print("result: ${it}")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }
}