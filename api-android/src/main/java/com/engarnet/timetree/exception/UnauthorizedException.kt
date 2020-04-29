package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class UnauthorizedException(error: TError) : TimeTreeApiException(error)