package com.engarnet.timetree.exception

import com.engarnet.timetree.model.TError

class ServiceUnavailableException(error: TError) : TimeTreeApiException(error)