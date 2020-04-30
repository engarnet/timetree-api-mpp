package com.engarnet.timetree.model

import android.net.Uri
import com.engarnet.timetree.api.v1.api.events.response.EventResponse
import com.engarnet.timetree.api.v1.api.events.response.UpcomingEventsResponse
import com.engarnet.timetree.model.type.Category
import java.util.*

data class TEvent(
    val id: String,
    val isKeep: Boolean,
    val title: String,
    val allDay: Boolean,
    val startAt: Date,
    val startTimezone: TimeZone,
    val endAt: Date,
    val endTimezone: TimeZone,
    val recurrence: List<Unit>?,
    val recurringUuid: String?,
    val description: String?,
    val location: String?,
    val url: Uri?,
    val creator: TUser,
    val label: TLabel,
    val attendees: List<TUser>,
    val createdAt: Date,
    val updatedAt: Date
)

internal fun EventResponse.toModel(): TEvent {
    return data.let { entity ->
        TEvent(
            id = entity.id,
            isKeep = entity.attributes.category == Category.Keep,
            title = entity.attributes.title,
            allDay = entity.attributes.allDay,
            startAt = Date().apply { time = entity.attributes.startAt.time },
            startTimezone = TimeZone.getTimeZone(entity.attributes.startTimezone),
            endAt = Date().apply { time = entity.attributes.endAt.time },
            endTimezone = TimeZone.getTimeZone(entity.attributes.endTimezone),
            recurrence = entity.attributes.recurrence,
            recurringUuid = entity.attributes.recurringUuid,
            description = entity.attributes.description,
            location = entity.attributes.location,
            url = entity.attributes.url?.let { Uri.parse(it) },
            creator = entity.relationships.creator.data.id.let { id ->
                included.firstOrNull { it.id == id }?.let {
                    TUser(
                        id = it.id,
                        name = it.attributes.name,
                        description = it.attributes.description,
                        imageUrl = it.attributes.imageUrl?.let { Uri.parse(it) }
                    )
                } ?: TUser(id, "workaround name", "", imageUrl = null) // TODO: includedにmembersが返って来ない場合の回避策
            },
            label = entity.relationships.label.data.id.let { id ->
                included.firstOrNull { it.id == id }?.let {
                    TLabel(
                        id = it.id,
                        name = it.attributes.name,
                        color = it.attributes.color!!
                    )
                } ?: TLabel(id, "workaround name", 0) // TODO: includedにlabelsが返って来ないので回避策
            },
            attendees = entity.relationships.attendees.data.map { entity ->
                entity.id
            }.map { attendeesId ->
                included.firstOrNull { it.id == attendeesId }?.let {
                    TUser(
                        id = it.id,
                        name = it.attributes.name,
                        description = it.attributes.description,
                        imageUrl = it.attributes.imageUrl?.let { Uri.parse(it) }
                    )
                } ?: TUser(attendeesId, "workaround name", "", imageUrl = null) // TODO: includedにmembersが返って来ない場合の回避策
            },
            createdAt = Date().apply { time = entity.attributes.createdAt.time },
            updatedAt = Date().apply { time = entity.attributes.updatedAt.time }
        )
    }
}


internal fun UpcomingEventsResponse.toModel(): List<TEvent> {
    return data.map { entity ->
        TEvent(
            id = entity.id,
            isKeep = entity.attributes.category == Category.Keep,
            title = entity.attributes.title,
            allDay = entity.attributes.allDay,
            startAt = Date().apply { time = entity.attributes.startAt.time },
            startTimezone = TimeZone.getTimeZone(entity.attributes.startTimezone),
            endAt = Date().apply { time = entity.attributes.endAt.time },
            endTimezone = TimeZone.getTimeZone(entity.attributes.endTimezone),
            recurrence = entity.attributes.recurrence,
            recurringUuid = entity.attributes.recurringUuid,
            description = entity.attributes.description,
            location = entity.attributes.location,
            url = entity.attributes.url?.let { Uri.parse(it) },
            creator = entity.relationships.creator.data.id.let { id ->
                included.firstOrNull { it.id == id }?.let {
                    TUser(
                        id = it.id,
                        name = it.attributes.name,
                        description = it.attributes.description,
                        imageUrl = it.attributes.imageUrl?.let { Uri.parse(it) }
                    )
                } ?: TUser(id, "workaround name", "", imageUrl = null) // TODO: includedにmembersが返って来ない場合の回避策
            },
            label = entity.relationships.label.data.id.let { id ->
                included.firstOrNull { it.id == id }?.let {
                    TLabel(
                        id = it.id,
                        name = it.attributes.name,
                        color = it.attributes.color!!
                    )
                } ?: TLabel(id, "workaround name", 0) // TODO: includedにlabelsが返って来ないので回避策
            },
            attendees = entity.relationships.attendees.data.map { entity ->
                entity.id
            }.map { attendeesId ->
                included.firstOrNull { it.id == attendeesId }?.let {
                    TUser(
                        id = it.id,
                        name = it.attributes.name,
                        description = it.attributes.description,
                        imageUrl = it.attributes.imageUrl?.let { Uri.parse(it) }
                    )
                } ?: TUser(attendeesId, "workaround name", "", imageUrl = null) // TODO: includedにmembersが返って来ない場合の回避策
            },
            createdAt = Date().apply { time = entity.attributes.createdAt.time },
            updatedAt = Date().apply { time = entity.attributes.updatedAt.time }
        )
    }
}