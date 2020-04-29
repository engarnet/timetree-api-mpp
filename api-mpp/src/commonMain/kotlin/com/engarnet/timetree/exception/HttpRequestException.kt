package com.engarnet.timetree.exception

import com.engarnet.timetree.api.v1.entity.ErrorEntity

class HttpRequestException(val responseCode: Int, val error: ErrorEntity) : Exception()