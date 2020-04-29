package com.engarnet.timetree.api.v1.entity

import com.engarnet.timetree.api.v1.serializer.UriSerializer
import com.engarnet.timetree.model.type.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: String,
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val name: String,
        val description: String,
        @SerialName("image_url") @Serializable(with = UriSerializer::class) val imageUrl: Uri?
    )
}