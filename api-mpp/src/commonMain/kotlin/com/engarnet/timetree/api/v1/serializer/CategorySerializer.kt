package com.engarnet.timetree.api.v1.serializer

import com.engarnet.timetree.api.v1.entity.ext.toCategory
import com.engarnet.timetree.api.v1.entity.ext.toParam
import com.engarnet.timetree.model.type.Category
import kotlinx.serialization.*

@Serializer(forClass = Category::class)
object CategorySerializer : KSerializer<Category> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Category) {
        encoder.encodeString(value.toParam())
    }

    override fun deserialize(decoder: Decoder): Category {
        return decoder.decodeString().toCategory()
    }
}