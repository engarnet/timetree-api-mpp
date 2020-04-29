package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

open class TimeTreeApiException(val error: TError) : Exception()