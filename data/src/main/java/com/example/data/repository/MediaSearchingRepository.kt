package com.example.data.repository

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import javax.inject.Inject

class MediaSearchingRepository @Inject constructor(
    private val mediaSearchingRemoteDataSource: MediaSearchingRemoteDataSource
) {
    suspend fun getImageResult(
        query: String,
        page: Int,
        size: Int
    ) = mediaSearchingRemoteDataSource.searchImageResult(
        query = query,
        page = page,
        size = size
    )

    suspend fun getVideoResult(
        query: String,
        page: Int,
        size: Int
    ) = mediaSearchingRemoteDataSource.searchVideoResult(
        query = query,
        page = page,
        size = size
    )
}