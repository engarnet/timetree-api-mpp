package com.engarnet.timetree.api.v1.api.activities.params

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class AddActivityParams(
    val calendarId: String,
    val eventId: String,
    val content: String
) {
    fun toJson(): String {
        val param = Params(
            data = Params.ActivityItem(
                attributes = Params.ActivityItem.Attributes(
                    content = content
                )
            )
        )

        return Json.stringify(Params.serializer(), param)
    }

    @Serializable
    private data class Params(val data: ActivityItem) {
        @Serializable
        data class ActivityItem(
            val attributes: Attributes
        ) {
            @Serializable
            data class Attributes(
                val content: String
            )
        }
    }
}
