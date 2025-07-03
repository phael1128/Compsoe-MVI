package com.example.data.model

sealed class ResponseResult<T> {
    data class Success<T>(
        val body: T,
    ) : ResponseResult<T>()

    data class Fail<T>(
        val errorCode: Int,
        val errorMessage: String,
    ) : ResponseResult<T>()
}
