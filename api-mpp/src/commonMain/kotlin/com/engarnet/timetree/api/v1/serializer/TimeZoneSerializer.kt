package com.engarnet.timetree.api.v1.serializer

import com.engarnet.timetree.type.TimeZone
import kotlinx.serialization.*

@Serializer(forClass = TimeZone::class)
object TimeZoneSerializer : KSerializer<TimeZone> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TimeZone) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): TimeZone {
        return decoder.decodeString()
    }
}