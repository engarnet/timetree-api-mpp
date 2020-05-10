package com.engarnet.timetree.api.client.impl

import com.engarnet.timetree.api.client.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection

actual class DefaultApiClient actual constructor(private val accessToken: String) : ApiClient {
    actual val headers: Map<String, String>
        get() {
            return if (accessToken.isEmpty()) mapOf() else mapOf("Authorization" to "Bearer $accessToken")
        }

    actual override suspend fun get(
        path: String,
        headers: Map<String, String>,
        params: Map<String, Any>
    ): String =
        withContext(Dispatchers.IO) {
            val queryParams = params.map { "${it.key}=${it.value}" }.joinToString("&")
            val url = URL("$path?$queryParams")
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connect(connection, headers)
        }

    actual override suspend fun post(
        path: String,
        headers: Map<String, String>,
        body: String
    ): String =
        withContext(Dispatchers.IO) {
            val url = URL(path)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connect(connection, headers, body)
        }

    actual override suspend fun put(
        path: String,
        headers: Map<String, String>,
        body: String
    ): String =
        withContext(Dispatchers.IO) {
            val url = URL(path)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "PUT"
            connection.doOutput = true
            connect(connection, headers, body)
        }

    actual override suspend fun delete(path: String, headers: Map<String, String>): String =
        withContext(Dispatchers.IO) {
            val url = URL(path)
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "DELETE"
            connect(connection, headers)
        }

    private suspend fun connect(
        connection: HttpsURLConnection,
        headers: Map<String, String>,
        body: String? = null
    ): String =
        withContext(Dispatchers.IO) {
            connection.instanceFollowRedirects = false
            (headers + this@DefaultApiClient.headers).forEach { entry ->
                connection.setRequestProperty(entry.key, entry.value)
            }

            val json: String
            try {
                connection.connect()

                body?.let {
                    print("body: $it")
                    PrintStream(connection.outputStream).print(it)
                }

                handleError(
                    responseCode = connection.responseCode,
                    body = connection.errorStream?.toJson()
                )

                json = BufferedInputStream(connection.inputStream).toJson()
            } finally {
                connection.disconnect()
            }
            json
        }

    private fun InputStream.toJson(): String {
        val br = BufferedReader(InputStreamReader(this))
        val sb = StringBuilder()

        while (true) {
            val line = br.readLine() ?: break
            sb.append(line)
        }
        br.close()
        return sb.toString()
    }
}