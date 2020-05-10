package com.engarnet.timetree.api.v1.api

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.platform.api.client.DefaultApiClient
import com.engarnet.timetree.api.v1.api.calendars.CalendarApi
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarLabelsParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarMembersParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarsParams
import com.engarnet.timetree.type.Include
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CalendarApiTest {
    private lateinit var apiClient: ApiClient

    private val accessToken = ""
    private val calendarId = ""

    @Before
    fun setup() {
        apiClient = DefaultApiClient(
            accessToken
        )
    }

    @Test
    fun test_calendars() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarsParams(
                include = Include.Calendars(labels = true, members = true)
            ).runCatching {
                api.calendars(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_calendar() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarParams(
                calendarId = calendarId,
                include = Include.Calendars(labels = true, members = true)
            ).runCatching {
                api.calendar(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_calendarLabels() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarLabelsParams(
                calendarId = calendarId
            ).runCatching {
                api.calendarLabels(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_calendarMembers() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarMembersParams(
                calendarId = calendarId
            ).runCatching {
                api.calendarMembers(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }
}