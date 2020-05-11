package com.engarnet.timetree

import android.net.Uri
import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.platform.api.client.DefaultApiClient
import com.engarnet.timetree.api.v1.api.activity.ActivityApi
import com.engarnet.timetree.api.v1.api.activity.params.AddActivityParams
import com.engarnet.timetree.api.v1.api.calendars.CalendarApi
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarLabelsParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarMembersParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarsParams
import com.engarnet.timetree.api.v1.api.events.EventsApi
import com.engarnet.timetree.api.v1.api.events.params.*
import com.engarnet.timetree.api.v1.api.user.UserApi
import com.engarnet.timetree.exception.*
import com.engarnet.timetree.model.*
import com.engarnet.timetree.type.Include
import java.util.*

class TimeTreeClient(
    accessToken: String
) {
    private val apiClient: ApiClient =
        DefaultApiClient(accessToken)
    private val userApi: UserApi = UserApi(apiClient)
    private val calendarApi: CalendarApi = CalendarApi(apiClient)
    private val eventsApi: EventsApi = EventsApi(apiClient)
    private val activitiesApi: ActivityApi =
        ActivityApi(apiClient)

    private fun HttpRequestException.convertDetail() {
        val error = error.toModel(responseCode)
        when (responseCode) {
            400 -> throw BadRequestException(error)
            401 -> throw UnauthorizedException(error)
            403 -> throw ForbiddenException(error)
            404 -> throw NotFoundException(error)
            406 -> throw NotAcceptableException(error)
            429 -> throw TooManyRequestException(error)
            500 -> throw InternalServerErrorException(error)
            503 -> throw ServiceUnavailableException(error)
            504 -> throw TimeoutException(error)
            else -> throw IllegalStateException("unknown error")
        }
    }

    private suspend fun <T> call(procedure: suspend () -> T): T {
        return runCatching {
            procedure()
        }.onFailure {
            if (it is HttpRequestException) {
                it.convertDetail()
            } else {
                throw it
            }
        }.getOrThrow()

    }

    suspend fun user(): TUser = call {
        userApi.user().toModel()
    }

    suspend fun calendars(): List<TCalendar> = call {
        calendarApi.calendars(
            CalendarsParams(
                Include.Calendars(labels = true, members = true)
            )
        ).toModel()
    }

    suspend fun calendar(calendarId: String): TCalendar = call {
        calendarApi.calendar(
            CalendarParams(
                calendarId = calendarId,
                include = Include.Calendars(labels = true, members = true)
            )
        ).toModel()
    }

    suspend fun calendarLabels(calendarId: String): List<TLabel> = call {
        calendarApi.calendarLabels(
            CalendarLabelsParams(calendarId = calendarId)
        ).toModel()
    }

    suspend fun calendarMembers(calendarId: String): List<TUser> = call {
        calendarApi.calendarMembers(
            CalendarMembersParams(calendarId = calendarId)
        ).toModel()
    }

    suspend fun event(calendarId: String, eventId: String): TEvent = call {
        eventsApi.event(
            EventParams(
                calendarId = calendarId,
                eventId = eventId,
                include = Include.Events(label = true, creator = true, attendees = true)
            )
        ).toModel()
    }

    suspend fun upcomingEvents(calendarId: String, timezone: TimeZone): List<TEvent> = call {
        eventsApi.upcomingEvents(
            UpcomingEventsParams(
                calendarId = calendarId,
                days = 7,
                include = Include.Events(label = true, creator = true, attendees = true),
                timeZone = timezone.id
            )
        ).toModel()
    }

    suspend fun addSchedule(
        calendarId: String,
        title: String,
        allDay: Boolean,
        startAt: Date,
        endAt: Date,
        timeZone: TimeZone? = null,
        description: String?,
        location: String?,
        url: Uri?,
        label: TLabel,
        attendees: List<TUser>?
    ): TEvent = call {
        val response = eventsApi.addEvent(
            AddEventsParams.AddScheduleParams(
                calendarId = calendarId,
                title = title,
                allDay = allDay,
                startAt = startAt.let {
                    com.engarnet.timetree.type.Date().apply { time = it.time }
                },
                startTimeZone = timeZone?.id,
                endAt = endAt.let {
                    com.engarnet.timetree.type.Date().apply { time = it.time }
                },
                endTimeZone = timeZone?.id,
                description = description,
                location = location,
                url = url?.toString(),
                labelId = label.id,
                attendees = attendees?.map { it.id }
            )
        )
        event(
            calendarId = calendarId,
            eventId = response.data.id
        )
    }

    suspend fun addKeep(
        calendarId: String,
        title: String,
        description: String?,
        location: String?,
        url: Uri?,
        label: TLabel,
        attendees: List<TUser>?
    ): TEvent = call {
        val response = eventsApi.addEvent(
            AddEventsParams.AddKeepParams(
                calendarId = calendarId,
                title = title,
                description = description,
                allDay = false, // TODO: Keepの場合allDayはOptionalだがnullを渡すと400エラーになるので設定
                location = location,
                url = url?.toString(),
                labelId = label.id,
                attendees = attendees?.map { it.id }
            )
        )
        event(
            calendarId = calendarId,
            eventId = response.data.id
        )
    }

    suspend fun updateSchedule(
        calendarId: String,
        eventId: String,
        title: String,
        allDay: Boolean,
        startAt: Date,
        endAt: Date,
        timeZone: TimeZone? = null,
        description: String?,
        location: String?,
        url: Uri?,
        label: TLabel,
        attendees: List<TUser>?
    ): TEvent = call {
        val response = eventsApi.updateEvent(
            UpdateEventParams(
                eventId = eventId,
                addEventsParams = AddEventsParams.AddScheduleParams(
                    calendarId = calendarId,
                    title = title,
                    allDay = allDay,
                    startAt = startAt.let {
                        com.engarnet.timetree.type.Date().apply { time = it.time }
                    },
                    startTimeZone = timeZone?.id,
                    endAt = endAt.let {
                        com.engarnet.timetree.type.Date().apply { time = it.time }
                    },
                    endTimeZone = timeZone?.id,
                    description = description,
                    location = location,
                    url = url?.toString(),
                    labelId = label.id,
                    attendees = attendees?.map { it.id }
                )
            )
        )
        event(
            calendarId = calendarId,
            eventId = response.data.id
        )
    }

    suspend fun updateKeep(
        calendarId: String,
        eventId: String,
        title: String,
        description: String?,
        location: String?,
        url: Uri?,
        label: TLabel,
        attendees: List<TUser>?
    ): TEvent = call {
        val response = eventsApi.updateEvent(
            UpdateEventParams(
                eventId = eventId,
                addEventsParams = AddEventsParams.AddKeepParams(
                    calendarId = calendarId,
                    title = title,
                    description = description,
                    allDay = false, // TODO: Keepの場合allDayはOptionalだがnullを渡すと400エラーになるので設定
                    location = location,
                    url = url?.toString(),
                    labelId = label.id,
                    attendees = attendees?.map { it.id }
                )
            )
        )
        event(
            calendarId = calendarId,
            eventId = response.data.id
        )
    }

    suspend fun deleteEvent(calendarId: String, eventId: String) = call {
        eventsApi.deleteEvent(
            DeleteEventsParams(
                calendarId = calendarId,
                eventId = eventId
            )
        )
    }

    suspend fun addComment(calendarId: String, eventId: String, comment: String): TComment = call {
        activitiesApi.addActivity(
            AddActivityParams(
                calendarId = calendarId,
                eventId = eventId,
                content = comment
            )
        ).toModel()
    }
}