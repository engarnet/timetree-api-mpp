package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class NotFoundException(error: TError) : TimeTreeApiException(error)