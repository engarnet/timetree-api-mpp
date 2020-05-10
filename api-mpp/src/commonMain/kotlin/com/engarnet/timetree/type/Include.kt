package com.engarnet.timetree.type

sealed class Include {
    data class Calendars(val labels: Boolean, val members: Boolean)
    data class Events(val label: Boolean, val creator: Boolean, val attendees: Boolean)
}