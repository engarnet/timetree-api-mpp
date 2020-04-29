package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class TooManyRequestException(error: TError) : TimeTreeApiException(error)