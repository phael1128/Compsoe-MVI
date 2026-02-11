package com.example.data.repository

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.mapping.toImage
import com.example.data.mapping.toVideo
import com.example.domain.entity.Image
import com.example.domain.entity.Video
import com.example.domain.model.DomainResult
import com.example.domain.repository.MediaSearchingRepository
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
        ): DomainResult<Image> {
            val responseImage =
                mediaSearchingRemoteDataSource.searchImageResult(
                    query = query,
                    page = page,
                    size = size,
                )

            return if (responseImage.isSuccessful) {
                responseImage.body()?.let { response ->
                    DomainResult.Success(response.toImage())
                } ?: DomainResult.Fail(responseImage.code(), responseImage.message())
            } else {
                DomainResult.Fail(responseImage.code(), responseImage.message())
            }
        }

        override suspend fun getVideoResult(
            query: String,
            page: Int,
            size: Int,
        ): DomainResult<Video> {
            val responseVideo =
                mediaSearchingRemoteDataSource.searchVideoResult(
                    query = query,
                    page = page,
                    size = size,
                )

            return if (responseVideo.isSuccessful) {
                responseVideo.body()?.let { response ->
                    DomainResult.Success(response.toVideo())
                } ?: DomainResult.Fail(responseVideo.code(), responseVideo.message())
            } else {
                DomainResult.Fail(responseVideo.code(), responseVideo.message())
            }
        }
    }
