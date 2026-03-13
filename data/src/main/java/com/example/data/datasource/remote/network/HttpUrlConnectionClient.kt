package com.example.data.datasource.remote.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class HttpUrlConnectionClient
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val gson: Gson,
    ) {
        suspend fun <T> get(
            path: String,
            queryParams: Map<String, String>,
            responseClass: Class<T>,
        ): RemoteResponse<T> =
            withContext(Dispatchers.IO) {
                if (!hasNetwork()) {
                    return@withContext RemoteResponse.failure(
                        code = NETWORK_ERROR_CODE,
                        message = NO_NETWORK_MESSAGE,
                    )
                }

                val connection = openGetConnection(path, queryParams)

                try {
                    val responseCode = connection.responseCode
                    val responseMessage = connection.responseMessage.orEmpty()
                    val responseBody = readResponseBody(connection, responseCode)
                    val parsedBody =
                        if (responseCode in SUCCESSFUL_RESPONSE_RANGE && responseBody != null) {
                            runCatching {
                                gson.fromJson(responseBody, responseClass)
                            }.getOrNull()
                        } else {
                            null
                        }

                    if (responseCode in SUCCESSFUL_RESPONSE_RANGE && parsedBody == null) {
                        return@withContext RemoteResponse.failure(
                            code = responseCode,
                            message = EMPTY_RESPONSE_MESSAGE,
                        )
                    }

                    parsedBody?.let {
                        return@withContext RemoteResponse.success(
                            code = responseCode,
                            message = responseMessage.ifBlank { DEFAULT_ERROR_MESSAGE },
                            body = it,
                        )
                    }

                    RemoteResponse.failure(
                        code = responseCode,
                        message = responseMessage.ifBlank { DEFAULT_ERROR_MESSAGE },
                    )
                } catch (throwable: Throwable) {
                    RemoteResponse.failure(
                        code = NETWORK_ERROR_CODE,
                        message = throwable.message ?: DEFAULT_ERROR_MESSAGE,
                    )
                } finally {
                    connection.disconnect()
                }
            }

        private fun openGetConnection(
            path: String,
            queryParams: Map<String, String>,
        ): HttpURLConnection =
            (URL(buildRequestUrl(path, queryParams)).openConnection() as HttpURLConnection).apply {
                requestMethod = GET_METHOD
                connectTimeout = CONNECT_TIMEOUT_MS
                readTimeout = READ_TIMEOUT_MS
                doInput = true
                setRequestProperty(HOST_HEADER_NAME, HOST_HEADER_VALUE)
                setRequestProperty(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                setRequestProperty(CACHE_CONTROL_HEADER_NAME, CACHE_CONTROL_HEADER_VALUE)
            }

        private fun buildRequestUrl(
            path: String,
            queryParams: Map<String, String>,
        ): String {
            val uriBuilder = Uri.parse(BASE_URL).buildUpon()

            path.trim('/').split('/').forEach(uriBuilder::appendPath)
            queryParams.forEach { (key, value) ->
                uriBuilder.appendQueryParameter(key, value)
            }

            return uriBuilder.build().toString()
        }

        private fun readResponseBody(
            connection: HttpURLConnection,
            responseCode: Int,
        ): String? {
            val stream =
                if (responseCode in SUCCESSFUL_RESPONSE_RANGE) {
                    connection.inputStream
                } else {
                    connection.errorStream
                } ?: return null

            return BufferedInputStream(stream).bufferedReader().use { reader ->
                reader.readText().takeIf { it.isNotBlank() }
            }
        }

        private fun hasNetwork(): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        companion object {
            private const val BASE_URL = "https://dapi.kakao.com"
            private const val GET_METHOD = "GET"
            private const val HOST_HEADER_NAME = "Host"
            private const val HOST_HEADER_VALUE = "dapi.kakao.com"
            private const val AUTHORIZATION_HEADER_NAME = "Authorization"
            private const val AUTHORIZATION_HEADER_VALUE = "KakaoAK 97e6e3cc8b4ae9a058df4f56ccd8844b"
            private const val CACHE_CONTROL_HEADER_NAME = "Cache-control"
            private const val CACHE_CONTROL_HEADER_VALUE = "public, max-age=300"
            private const val CONNECT_TIMEOUT_MS = 15_000
            private const val READ_TIMEOUT_MS = 15_000
            private const val NETWORK_ERROR_CODE = -1
            private const val NO_NETWORK_MESSAGE = "No network connection"
            private const val EMPTY_RESPONSE_MESSAGE = "Empty response body"
            private const val DEFAULT_ERROR_MESSAGE = "Network request failed"
            private val SUCCESSFUL_RESPONSE_RANGE = 200..299
        }
    }
