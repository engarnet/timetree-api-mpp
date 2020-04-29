package com.engarnet.timetree.api.v1.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorEntity(
    val type: String? = null,
    val status: Int? = null,
    val title: String? = null,
    val errors: Map<String, List<String>>? = null,
    val error: String? = null,
    @SerialName("error_description") val errorDescription: String? = null
)