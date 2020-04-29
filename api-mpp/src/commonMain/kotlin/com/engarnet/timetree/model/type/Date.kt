package com.engarnet.timetree.model.type

import com.soywiz.klock.DateTime

class Date {
    internal var dateTime = DateTime(0)

    var time: Long
        get() = dateTime.unixMillisLong
        set(value) {
            dateTime = DateTime(value)
        }

    override fun toString(): String = dateTime.toString()
}
