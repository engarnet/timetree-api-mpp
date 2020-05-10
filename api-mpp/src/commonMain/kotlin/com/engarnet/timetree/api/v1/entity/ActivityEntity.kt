package com.engarnet.timetree.api.v1.entity

import com.engarnet.timetree.api.v1.serializer.DateSerializer
import com.engarnet.timetree.type.Date
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityEntity(
    val id: String,
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val content: String,
        @SerialName("updated_at") @Serializable(with = DateSerializer::class) val updatedAt: Date,
        @SerialName("created_at") @Serializable(with = DateSerializer::class) val createdAt: Date
    )
}