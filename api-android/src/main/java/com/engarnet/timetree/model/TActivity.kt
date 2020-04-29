package com.engarnet.timetree.model

import com.engarnet.timetree.api.v1.api.activities.response.ActivityResponse
import java.util.*

data class TActivity(
    val id: String,
    val content: String,
    val updatedAt: Date,
    val createdAt: Date
)

internal fun ActivityResponse.toModel(): TActivity {
    return data.let {
        TActivity(
            id = it.id,
            content = it.attributes.content,
            updatedAt = Date().apply { time = it.attributes.updatedAt.time },
            createdAt = Date().apply { time = it.attributes.createdAt.time }
        )
    }
}