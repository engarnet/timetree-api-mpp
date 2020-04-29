package com.engarnet.timetree.api.v1.serializer

import kotlinx.serialization.*

@Serializer(forClass = Int::class)
object ColorSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Int) {
        val red = value shr 16 and 0xFF
        val green = value shr 8 and 0xFF
        val blue = value and 0xFF
        encoder.encodeString("#${red.toHex()}${green.toHex()}${blue.toHex()}")
    }

    override fun deserialize(decoder: Decoder): Int {
        val colorString = decoder.decodeString()
        // Use a long to avoid rollovers on #ffXXXXXX
        // Use a long to avoid rollovers on #ffXXXXXX
        var color: Long = colorString.substring(1).toLong(16)
        if (colorString.length == 7) { // Set the alpha value
            color = color or -0x1000000
        } else require(colorString.length == 9) { "Unknown color" }
        return color.toInt()
    }

    private fun Int.toHex(): String {
        return ("0${this.toString(16)}").let {
            it.substring(it.length - 2)
        }
    }
}