package com.engarnet.timetree.api.v1.serializer

import com.engarnet.timetree.type.Date
import com.soywiz.klock.DateException
import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import kotlinx.serialization.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    private val dateFormatMillis = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private val dateFormatSeconds = DateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")


    override fun serialize(output: Encoder, obj: Date) {
        output.encodeString(obj.dateTime.format(dateFormatMillis))
    }

    override fun deserialize(input: Decoder): Date {
        val json = input.decodeString()
        return try {
            Date().apply { dateTime = dateFormatMillis.parse(json).local }
        } catch (e: DateException) {
            Date().apply { dateTime = dateFormatSeconds.parse(json).local }
        }
    }
}