package com.engarnet.timetree.api.v1.api

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.platform.api.client.DefaultApiClient
import com.engarnet.timetree.api.v1.api.user.UserApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UserApiTest {
    private lateinit var apiClient: ApiClient
    private val accessToken = ""

    @Before
    fun setup() {
        apiClient = DefaultApiClient(
            accessToken
        )
    }

    @Test
    fun test_user() = runBlocking {
        val api = UserApi(apiClient)
        val result = api.user()
        print("result: ${result}")
    }
}