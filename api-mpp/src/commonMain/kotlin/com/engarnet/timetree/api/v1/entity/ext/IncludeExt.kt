package com.engarnet.timetree.api.v1.entity.ext

import com.engarnet.timetree.model.type.Include

fun Include.Calendars.toParam(): String {
    return listOf(
        if (labels) "labels" else "",
        if (members) "members" else ""
    ).filter { it != "" }.joinToString(",")
}

fun Include.Events.toParam(): String {
    return listOf(
        if (labels) "labels" else "",
        if (creator) "creator" else "",
        if (attendees) "attendees" else ""
    ).filter { it != "" }.joinToString(",")
}