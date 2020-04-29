package com.engarnet.timetree.api.config

sealed class Config {
    object V1 {
        const val apiUrl = "https://timetreeapis.com"
        const val authorizeUrl = "https://timetreeapp.com/oauth/authorize"
        const val tokenUrl = "https://timetreeapp.com/oauth/token"
        val headers = mapOf(
            "Content-Type" to "application/json",
            "Accept" to "application/vnd.timetree.v1+json"
        )
    }
}