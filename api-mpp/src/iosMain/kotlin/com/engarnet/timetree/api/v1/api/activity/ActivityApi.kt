package com.engarnet.timetree.api.v1.api.activity

import com.engarnet.timetree.api.v1.api.activity.params.AddActivityParams
import com.engarnet.timetree.api.v1.api.activity.response.ActivityResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun ActivityApi.addActivity(params: AddActivityParams): Deferred<ActivityResponse> {
    return GlobalScope.async {
        addActivity(params)
    }
}
