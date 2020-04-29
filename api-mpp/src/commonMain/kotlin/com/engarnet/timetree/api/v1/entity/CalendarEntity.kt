package com.engarnet.timetree.api.v1.entity

import com.engarnet.timetree.api.v1.serializer.ColorSerializer
import com.engarnet.timetree.api.v1.serializer.DateSerializer
import com.engarnet.timetree.api.v1.serializer.UriSerializer
import com.engarnet.timetree.model.type.Date
import com.engarnet.timetree.model.type.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarEntity(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    @Serializable
    data class Attributes(
        val name: String,
        val description: String,
        @Serializable(with = ColorSerializer::class) val color: Int,
        val order: Int,
        @SerialName("image_url") @Serializable(with = UriSerializer::class) val imageUrl: Uri?,
        @SerialName("created_at") @Serializable(with = DateSerializer::class) val createdAt: Date
    )

    @Serializable
    data class Relationships(
        val labels: Labels,
        val members: Members
    ) {
        @Serializable
        data class Labels(
            val data: List<DataEntity>
        )

        @Serializable
        data class Members(
            val data: List<DataEntity>
        )
    }
}

