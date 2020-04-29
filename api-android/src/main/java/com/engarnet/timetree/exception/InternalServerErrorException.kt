package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class InternalServerErrorException(error: TError) : TimeTreeApiException(error)