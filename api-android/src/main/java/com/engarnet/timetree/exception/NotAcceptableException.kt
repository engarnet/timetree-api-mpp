package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class NotAcceptableException(error: TError) : TimeTreeApiException(error)