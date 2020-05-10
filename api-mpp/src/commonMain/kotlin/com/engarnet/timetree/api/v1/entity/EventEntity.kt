package com.engarnet.timetree.api.v1.entity

import com.engarnet.timetree.api.v1.serializer.CategorySerializer
import com.engarnet.timetree.api.v1.serializer.DateSerializer
import com.engarnet.timetree.api.v1.serializer.TimeZoneSerializer
import com.engarnet.timetree.api.v1.serializer.UriSerializer
import com.engarnet.timetree.type.Category
import com.engarnet.timetree.type.Date
import com.engarnet.timetree.type.TimeZone
import com.engarnet.timetree.type.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventEntity(
    val id: String,
    val type: String,
    val attributes: Attributes,
    val relationships: Relationships
) {
    @Serializable
    data class Attributes(
        @Serializable(with = CategorySerializer::class) val category: Category,
        val title: String,
        @SerialName("all_day") val allDay: Boolean,
        @SerialName("start_at") @Serializable(with = DateSerializer::class) val startAt: Date,
        @SerialName("start_timezone") @Serializable(with = TimeZoneSerializer::class) val startTimezone: TimeZone,
        @SerialName("end_at") @Serializable(with = DateSerializer::class) val endAt: Date,
        @SerialName("end_timezone") @Serializable(with = TimeZoneSerializer::class) val endTimezone: TimeZone,
        val recurrence: List<Unit>?,
        @SerialName("recurring_uuid") val recurringUuid: String?, // TODO: 仕様にないパラメータ
        val description: String?,
        val location: String?,
        @SerialName("url") @Serializable(with = UriSerializer::class) val url: Uri?,
        @SerialName("updated_at") @Serializable(with = DateSerializer::class) val updatedAt: Date,
        @SerialName("created_at") @Serializable(with = DateSerializer::class) val createdAt: Date
    )

    @Serializable
    data class Relationships(
        val creator: Creator,
        val label: Label,
        val attendees: Attendees
    ) {
        @Serializable
        data class Creator(
            val data: DataEntity
        )

        @Serializable
        data class Label(
            val data: DataEntity
        )

        @Serializable
        data class Attendees(
            val data: List<DataEntity>
        )
    }
}