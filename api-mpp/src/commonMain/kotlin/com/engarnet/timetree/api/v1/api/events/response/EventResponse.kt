package com.engarnet.timetree.api.v1.api.events.response

import com.engarnet.timetree.api.v1.entity.EventEntity
import com.engarnet.timetree.api.v1.entity.IncludedEntity
import kotlinx.serialization.Serializable

@Serializable
data class EventResponse(
    val data: EventEntity,
    val included: List<IncludedEntity> = listOf() // TODO: 仕様にはないレスポンス
)