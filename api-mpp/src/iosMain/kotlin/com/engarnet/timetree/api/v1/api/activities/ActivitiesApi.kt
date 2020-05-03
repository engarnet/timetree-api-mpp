package com.engarnet.timetree.api.v1.api.activities

import com.engarnet.timetree.api.v1.api.activities.params.AddActivityParams
import com.engarnet.timetree.api.v1.api.activities.response.ActivityResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun ActivitiesApi.addActivity(params: AddActivityParams): Deferred<ActivityResponse> {
    return GlobalScope.async {
        addActivity(params)
    }
}
