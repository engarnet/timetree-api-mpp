package com.engarnet.timetree.api.v1.api

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.client.impl.DefaultApiClient
import com.engarnet.timetree.api.v1.api.events.EventsApi
import com.engarnet.timetree.api.v1.api.events.params.*
import com.engarnet.timetree.type.Date
import com.engarnet.timetree.type.Include
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class EventApiTest {
    private lateinit var apiClient: ApiClient

    private val accessToken = ""
    private val calendarId = ""
    private val eventId = ""

    @Before
    fun setup() {
        apiClient = DefaultApiClient(accessToken)
    }

    @Test
    fun test_events() {
        runBlocking {
            val api = EventsApi(apiClient)

            EventParams(
                calendarId = calendarId,
                eventId = eventId,
                include = Include.Events(
                    label = true,
                    creator = true,
                    attendees = true
                )
            ).runCatching {
                api.event(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_upcomingEvents() {
        runBlocking {
            val api = EventsApi(apiClient)

            UpcomingEventsParams(
                calendarId = calendarId,
                timeZone = TimeZone.getDefault().id,
                days = 7,
                include = Include.Events(
                    label = true,
                    creator = true,
                    attendees = true
                )
            ).runCatching {
                api.upcomingEvents(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_addEvent() {
        runBlocking {
            val api = EventsApi(apiClient)

            AddEventsParams.AddScheduleParams(
                calendarId = calendarId,
                title = "UnitTestEvent",
                allDay = false,
                startAt = Date().apply {
                    time = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }.timeInMillis
                },
                startTimeZone = TimeZone.getDefault().id,
                endAt = Date().apply {
                    time = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }.timeInMillis
                },
                endTimeZone = TimeZone.getDefault().id,
                description = "UnitTestEvent's description",
                location = "大阪駅",
                url = null,
                labelId = "$calendarId,1",
                attendees = listOf("hGL8r83T18UE,9271403242664018")
            ).runCatching {
                api.addEvent(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_updateEvent() {
        runBlocking {
            val api = EventsApi(apiClient)

            UpdateEventParams(
                eventId = eventId,
                addEventsParams = AddEventsParams.AddScheduleParams(
                    calendarId = calendarId,
                    title = "UnitTestEvent updated",
                    allDay = false,
                    startAt = Date().apply {
                        time = Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_YEAR, 1)
                        }.timeInMillis
                    },
                    startTimeZone = TimeZone.getDefault().id,
                    endAt = Date().apply {
                        time = Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_YEAR, 1)
                        }.timeInMillis
                    },
                    endTimeZone = TimeZone.getDefault().id,
                    description = "UnitTestEvent's description updated",
                    location = "大阪",
                    url = null,
                    labelId = "hGL8r83T18UE,1",
                    attendees = listOf("hGL8r83T18UE,9271403242664018")
                )
            ).runCatching {
                api.updateEvent(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test_deleteEvent() {
        runBlocking {
            val api = EventsApi(apiClient)

            DeleteEventsParams(
                calendarId = calendarId,
                eventId = eventId
            ).runCatching {
                api.deleteEvent(this)
            }.onSuccess {
                print("deleteEvent")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }
}