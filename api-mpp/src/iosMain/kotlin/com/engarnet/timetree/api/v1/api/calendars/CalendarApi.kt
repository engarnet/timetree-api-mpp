package com.engarnet.timetree.api.v1.api.calendars

import com.engarnet.timetree.api.v1.api.calendars.params.CalendarLabelsParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarMembersParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarsParams
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarLabelsResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarMembersResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarsResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun CalendarApi.calendars(params: CalendarsParams): Deferred<CalendarsResponse> {
    return GlobalScope.async {
        calendars(params)
    }
}

fun CalendarApi.calendar(params: CalendarParams): Deferred<CalendarResponse> {
    return GlobalScope.async {
        calendar(params)
    }
}

fun CalendarApi.calendarLabels(params: CalendarLabelsParams): Deferred<CalendarLabelsResponse> {
    return GlobalScope.async {
        calendarLabels(params)
    }
}

fun CalendarApi.calendarMembers(params: CalendarMembersParams): Deferred<CalendarMembersResponse> {
    return GlobalScope.async {
        calendarMembers(params)
    }
}