package com.example.data.repository

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.datasource.remote.response.ResImage
import com.example.data.datasource.remote.response.ResVideo
import com.example.data.model.ResponseResult
import javax.inject.Inject

class MediaSearchingRepositoryImpl
    @Inject
    constructor(
        private val mediaSearchingRemoteDataSource: MediaSearchingRemoteDataSource,
    ) : MediaSearchingRepository {
        override suspend fun getImageResult(
            query: String,
            page: Int,
            size: Int,
        ): ResponseResult<ResImage> {
            val responseImage =
                mediaSearchingRemoteDataSource.searchImageResult(
                    query = query,
                    page = page,
                    size = size,
                )

            val result =
                if (responseImage.isSuccessful) {
                    responseImage.body()?.let { response ->
                        ResponseResult.Success(response)
                    } ?: ResponseResult.Fail(responseImage.code(), responseImage.message())
                } else {
                    ResponseResult.Fail(responseImage.code(), responseImage.message())
                }

            return result
        }

        override suspend fun getVideoResult(
            query: String,
            page: Int,
            size: Int,
        ): ResponseResult<ResVideo> {
            val responseVideo =
                mediaSearchingRemoteDataSource.searchVideoResult(
                    query = query,
                    page = page,
                    size = size,
                )

            val result =
                if (responseVideo.isSuccessful) {
                    responseVideo.body()?.let { response ->
                        ResponseResult.Success(response)
                    } ?: ResponseResult.Fail(responseVideo.code(), responseVideo.message())
                } else {
                    ResponseResult.Fail(responseVideo.code(), responseVideo.message())
                }

            return result
        }
    }
