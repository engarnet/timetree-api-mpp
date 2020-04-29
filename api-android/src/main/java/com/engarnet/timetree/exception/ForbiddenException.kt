package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class ForbiddenException(error: TError) : TimeTreeApiException(error)