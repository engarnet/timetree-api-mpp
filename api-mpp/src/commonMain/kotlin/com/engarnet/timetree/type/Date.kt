package com.engarnet.timetree.type

import com.soywiz.klock.DateTime

class Date() {
    internal var dateTime = DateTime(0)

    constructor(time: Long) : this() {
        this.time = time
    }

    var time: Long
        get() = dateTime.unixMillisLong
        set(value) {
            dateTime = DateTime(value)
        }

    override fun toString(): String = dateTime.toString()
}
