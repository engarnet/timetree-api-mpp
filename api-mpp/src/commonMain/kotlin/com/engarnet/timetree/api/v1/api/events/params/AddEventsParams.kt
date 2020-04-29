package com.engarnet.timetree.api.v1.api.events.params

import com.engarnet.timetree.api.v1.entity.DataEntity
import com.engarnet.timetree.api.v1.serializer.CategorySerializer
import com.engarnet.timetree.api.v1.serializer.DateSerializer
import com.engarnet.timetree.api.v1.serializer.TimeZoneSerializer
import com.engarnet.timetree.api.v1.serializer.UriSerializer
import com.engarnet.timetree.model.type.Category
import com.engarnet.timetree.model.type.Date
import com.engarnet.timetree.model.type.TimeZone
import com.engarnet.timetree.model.type.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed class AddEventsParams(open val calendarId: String) {
    abstract fun toJson(): String

    data class AddScheduleParams(
        override val calendarId: String,
        val title: String,
        val allDay: Boolean,
        val startAt: Date,
        val startTimeZone: TimeZone? = null,
        val endAt: Date,
        val endTimeZone: TimeZone? = null,
        val description: String?,
        val location: String?,
        var url: Uri?,
        val labelId: String,
        val attendees: List<String>?
    ) : AddEventsParams(calendarId) {
        override fun toJson(): String {
            val param = Params(
                data = Params.EventItem(
                    attributes = Params.EventItem.Attributes(
                        category = Category.Schedule,
                        title = title,
                        allDay = allDay,
                        startAt = startAt,
                        startTimezone = startTimeZone,
                        endAt = endAt,
                        endTimezone = endTimeZone,
                        description = description,
                        location = location,
                        url = url
                    ),
                    relationships = Params.EventItem.Relationships(
                        label = Params.EventItem.Relationships.LabelItem(
                            data = DataEntity(id = labelId, type = "label")
                        ),
                        attendees = Params.EventItem.Relationships.Attendees(
                            data = attendees?.map { DataEntity(id = it, type = "user") } ?: listOf()
                        )
                    )
                )
            )

            return Json.stringify(Params.serializer(), param)
        }
    }

    data class AddKeepParams(
        override val calendarId: String,
        val title: String,
        val allDay: Boolean? = null,
        val startAt: Date? = null,
        val startTimeZone: TimeZone? = null,
        val endAt: Date? = null,
        val endTimeZone: TimeZone? = null,
        val description: String?,
        val location: String?,
        var url: Uri?,
        val labelId: String,
        val attendees: List<String>?
    ) : AddEventsParams(calendarId) {
        override fun toJson(): String {
            val param = Params(
                data = Params.EventItem(
                    attributes = Params.EventItem.Attributes(
                        category = Category.Keep,
                        title = title,
                        allDay = allDay,
                        startAt = startAt,
                        startTimezone = startTimeZone,
                        endAt = endAt,
                        endTimezone = endTimeZone,
                        description = description,
                        location = location,
                        url = url
                    ),
                    relationships = Params.EventItem.Relationships(
                        label = Params.EventItem.Relationships.LabelItem(
                            data = DataEntity(id = labelId, type = "label")
                        ),
                        attendees = Params.EventItem.Relationships.Attendees(
                            data = attendees?.map { DataEntity(id = it, type = "user") } ?: listOf()
                        )
                    )
                )
            )

            return Json.stringify(Params.serializer(), param)
        }
    }

    @Serializable
    private data class Params(val data: EventItem) {
        @Serializable
        data class EventItem(
            val attributes: Attributes,
            val relationships: Relationships
        ) {
            @Serializable
            data class Attributes(
                @Serializable(with = CategorySerializer::class) val category: Category,
                val title: String,
                @SerialName("all_day") val allDay: Boolean?,
                @SerialName("start_at") @Serializable(with = DateSerializer::class) val startAt: Date?,
                @SerialName("start_timezone") @Serializable(with = TimeZoneSerializer::class) val startTimezone: TimeZone?,
                @SerialName("end_at") @Serializable(with = DateSerializer::class) val endAt: Date?,
                @SerialName("end_timezone") @Serializable(with = TimeZoneSerializer::class) val endTimezone: TimeZone?,
                val description: String?,
                val location: String?,
                @SerialName("url") @Serializable(with = UriSerializer::class) val url: Uri?
            )

            @Serializable
            data class Relationships(
                val label: LabelItem,
                val attendees: Attendees
            ) {
                @Serializable
                data class LabelItem(val data: DataEntity)

                @Serializable
                data class Attendees(val data: List<DataEntity>)
            }
        }
    }
}