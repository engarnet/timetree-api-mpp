package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class TimeoutException(error: TError) : TimeTreeApiException(error)