package com.engarnet.timetree.model

import com.engarnet.timetree.api.v1.entity.ErrorEntity

data class TError(
    val responseCode: Int,
    val type: String?,
    val status: Int?,
    val title: String?,
    val errors: Map<String, List<String>>?,
    val error: String?,
    val errorDescription: String?
)

internal fun ErrorEntity.toModel(responseCode: Int): TError {
    val entity = this
    return TError(
        responseCode = responseCode,
        type = entity.type,
        status = entity.status,
        title = entity.title,
        errors = entity.errors,
        error = entity.error,
        errorDescription = entity.errorDescription
    )
}