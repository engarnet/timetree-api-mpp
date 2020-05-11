package com.engarnet.timetree.model

import com.engarnet.timetree.api.v1.api.activity.response.ActivityResponse
import java.util.*

data class TComment(
    val id: String,
    val content: String,
    val updatedAt: Date,
    val createdAt: Date
)

internal fun ActivityResponse.toModel(): TComment {
    return data.let {
        TComment(
            id = it.id,
            content = it.attributes.content,
            updatedAt = Date().apply { time = it.attributes.updatedAt.time },
            createdAt = Date().apply { time = it.attributes.createdAt.time }
        )
    }
}