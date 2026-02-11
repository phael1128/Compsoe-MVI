package com.example.domain.model

sealed class DomainResult<T> {
    data class Success<T>(
        val body: T,
    ) : DomainResult<T>()

    data class Fail<T>(
        val errorCode: Int,
        val errorMessage: String,
    ) : DomainResult<T>()
}
