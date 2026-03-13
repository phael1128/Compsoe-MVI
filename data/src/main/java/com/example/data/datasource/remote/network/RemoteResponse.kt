package com.example.data.datasource.remote.network

data class RemoteResponse<T>(
    val code: Int,
    val message: String,
    val body: T? = null,
) {
    val isSuccessful: Boolean
        get() = code in 200..299

    companion object {
        fun <T> success(
            code: Int,
            message: String,
            body: T,
        ): RemoteResponse<T> = RemoteResponse(code = code, message = message, body = body)

        fun <T> failure(
            code: Int,
            message: String,
        ): RemoteResponse<T> = RemoteResponse(code = code, message = message)
    }
}
