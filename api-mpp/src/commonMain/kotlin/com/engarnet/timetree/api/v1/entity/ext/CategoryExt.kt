package com.engarnet.timetree.api.v1.entity.ext

import com.engarnet.timetree.model.type.Category

fun Category.toParam(): String {
    return when (this) {
        Category.Schedule -> "schedule"
        Category.Keep -> "keep"
    }
}

fun String.toCategory(): Category {
    return when (this) {
        "schedule" -> Category.Schedule
        "keep" -> Category.Keep
        else -> throw IllegalStateException("invalid category name")
    }
}