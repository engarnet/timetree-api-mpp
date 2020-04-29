package com.engarnet.timetree.api.v1.entity

import kotlinx.serialization.Serializable

@Serializable
data class DataEntity(
    val id: String,
    val type: String
)