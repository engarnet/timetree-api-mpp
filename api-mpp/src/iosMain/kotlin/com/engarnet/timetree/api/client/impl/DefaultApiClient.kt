package com.engarnet.timetree.api.client.impl

import com.engarnet.timetree.api.client.ApiClient
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.*
import platform.darwin.NSObject

actual class DefaultApiClient actual constructor(private val accessToken: String) : ApiClient {
    actual val headers: Map<String, String>
        get() {
            return if (accessToken.isEmpty()) mapOf() else mapOf("Authorization" to "Bearer $accessToken")
        }

    actual override suspend fun get(
        path: String,
        headers: Map<String, String>,
        params: Map<String, Any>
    ): String = withContext(Dispatchers.Default) {
        val queryParams = params.map { "${it.key}=${it.value}" }.joinToString("&")
        val request = NSMutableURLRequest(uRL = NSURL(string = "$path?$queryParams"))
        request.HTTPMethod = "GET"

        connect(
            request = request,
            headers = headers,
            body = null
        )
    }

    actual override suspend fun post(
        path: String,
        headers: Map<String, String>,
        body: String
    ): String = withContext(Dispatchers.Default) {
        val request = NSMutableURLRequest(uRL = NSURL(string = path))
        request.HTTPMethod = "POST"

        connect(
            request = request,
            headers = headers,
            body = body
        )
    }

    actual override suspend fun put(
        path: String,
        headers: Map<String, String>,
        body: String
    ): String = withContext(Dispatchers.Default)  {
        val request = NSMutableURLRequest(uRL = NSURL(string = path))
        request.HTTPMethod = "PUT"

        connect(
            request = request,
            headers = headers,
            body = body
        )
    }

    actual override suspend fun delete(
        path: String,
        headers: Map<String, String>
    ): String = withContext(Dispatchers.Default)  {
        val request = NSMutableURLRequest(uRL = NSURL(string = path))
        request.HTTPMethod = "DELETE"

        connect(
            request = request,
            headers = headers,
            body = null
        )
    }

    private suspend fun connect(
        request: NSMutableURLRequest,
        headers: Map<String, String>,
        body: String?
    ): String = withContext(Dispatchers.Default) {
        (headers + this@DefaultApiClient.headers).forEach {
            request.setValue(it.value, forHTTPHeaderField = it.key)
        }

        body?.let {
            request.setHTTPBody(NSString.create(string = it).dataUsingEncoding(encoding = NSUTF8StringEncoding))
        }

        val delegate = Delegate()
        val session = NSURLSession.sessionWithConfiguration(
            configuration = NSURLSessionConfiguration.defaultSessionConfiguration,
            delegate = delegate,
            delegateQueue = NSOperationQueue.mainQueue
        )
        val task = session.dataTaskWithRequest(request)
        task.resume()

        val response = delegate.await()

        handleError(
            responseCode = response.httpUrlResponse.statusCode.toInt(),
            body = response.json
        )

        response.json
    }
}

internal class Delegate(
) : NSObject(),
    NSURLSessionDataDelegateProtocol {
    private val completableDeferred = CompletableDeferred<Response>()

    class Response(
        val httpUrlResponse: NSHTTPURLResponse,
        val json: String
    )

    suspend fun await(): Response {
        return completableDeferred.await()
    }

    override fun URLSession(
        session: NSURLSession,
        dataTask: NSURLSessionDataTask,
        didReceiveData: NSData
    ) {
        if (!completableDeferred.isCompleted) {
            val response = dataTask.response as NSHTTPURLResponse
            val json = NSString.create(didReceiveData, NSUTF8StringEncoding).toString()
            completableDeferred.complete(Response(response, json))
        }
    }

    override fun URLSession(
        session: NSURLSession,
        dataTask: NSURLSessionDataTask,
        didReceiveResponse: NSURLResponse,
        completionHandler: (NSURLSessionResponseDisposition) -> Unit
    ) {
        val response = dataTask.response as NSHTTPURLResponse
        if (response.statusCode.toInt() == 204) {
            // HTTP 204 No-Content
            val response = dataTask.response as NSHTTPURLResponse
            completableDeferred.complete(Response(response, ""))
        }
        completionHandler(NSURLSessionResponseAllow)
    }
}
