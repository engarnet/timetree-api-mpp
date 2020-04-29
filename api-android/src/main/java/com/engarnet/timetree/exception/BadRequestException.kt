package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class BadRequestException(error: TError) : TimeTreeApiException(error)