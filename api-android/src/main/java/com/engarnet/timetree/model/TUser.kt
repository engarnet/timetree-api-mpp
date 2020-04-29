package com.engarnet.timetree.model

import android.net.Uri
import com.engarnet.timetree.api.v1.api.calendars.response.CalendarMembersResponse
import com.engarnet.timetree.api.v1.api.user.response.UserResponse

data class TUser(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: Uri?
)

internal fun UserResponse.toModel(): TUser {
    return TUser(
        id = data.id,
        name = data.attributes.name,
        description = data.attributes.description,
        imageUrl = data.attributes.imageUrl?.let { Uri.parse(it) }
    )
}

internal fun CalendarMembersResponse.toModel(): List<TUser> {
    return data.map { user ->
        TUser(
            id = user.id,
            name = user.attributes.name,
            description = user.attributes.description,
            imageUrl = user.attributes.imageUrl?.let { Uri.parse(it) }
        )
    }
}