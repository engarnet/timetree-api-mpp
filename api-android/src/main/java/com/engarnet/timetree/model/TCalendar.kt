package com.engarnet.timetree.model

import android.net.Uri
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarResponse
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarsResponse
import java.util.*

data class TCalendar(
    val id: String,
    val name: String,
    val description: String,
    val color: Int,
    val order: Int,
    val imageUrl: Uri?,
    val createdAt: Date,
    val labels: List<TLabel>,
    val members: List<TUser>
)

internal fun CalendarResponse.toModel(): TCalendar {
    return data.let { entity ->
        TCalendar(
            id = entity.id,
            name = entity.attributes.name,
            description = entity.attributes.description,
            color = entity.attributes.color,
            order = entity.attributes.order,
            imageUrl = entity.attributes.imageUrl?.let { Uri.parse(it) },
            createdAt = Date().apply { time = entity.attributes.createdAt.time },
            labels = entity.relationships.labels.data
                .map { it.id }
                .mapNotNull { id -> included.firstOrNull { it.id == id } }
                .map { data ->
                    TLabel(
                        id = data.id,
                        name = data.attributes.name,
                        color = data.attributes.color!!
                    )
                },
            members = entity.relationships.members.data
                .map { it.id }
                .mapNotNull { id -> included.firstOrNull { it.id == id } }
                .map { data ->
                    TUser(
                        id = data.id,
                        name = data.attributes.name,
                        description = data.attributes.description,
                        imageUrl = data.attributes.imageUrl?.let {
                            Uri.parse(
                                it
                            )
                        }
                    )
                }
        )
    }
}

internal fun CalendarsResponse.toModel(): List<TCalendar> {
    return data.map { entity ->
        TCalendar(
            id = entity.id,
            name = entity.attributes.name,
            description = entity.attributes.description,
            color = entity.attributes.color,
            order = entity.attributes.order,
            imageUrl = entity.attributes.imageUrl?.let { Uri.parse(it) },
            createdAt = Date().apply { time = entity.attributes.createdAt.time },
            labels = entity.relationships.labels.data
                .map { it.id }
                .mapNotNull { id -> included.firstOrNull { it.id == id } }
                .map { data ->
                    TLabel(
                        id = data.id,
                        name = data.attributes.name,
                        color = data.attributes.color!!
                    )
                },
            members = entity.relationships.members.data
                .map { it.id }
                .mapNotNull { id -> included.firstOrNull { it.id == id } }
                .map { data ->
                    TUser(
                        id = data.id,
                        name = data.attributes.name,
                        description = data.attributes.description,
                        imageUrl = data.attributes.imageUrl?.let {
                            Uri.parse(
                                it
                            )
                        }
                    )
                }
        )
    }
}