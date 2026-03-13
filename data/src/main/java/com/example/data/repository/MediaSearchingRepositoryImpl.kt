package com.example.data.repository

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.datasource.remote.network.RemoteResponse
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
            return mediaSearchingRemoteDataSource.searchImageResult(
                query = query,
                page = page,
                size = size,
            ).toDomainResult { response ->
                response.toImage()
            }
        }

        override suspend fun getVideoResult(
            query: String,
            page: Int,
            size: Int,
        ): DomainResult<Video> {
            return mediaSearchingRemoteDataSource.searchVideoResult(
                query = query,
                page = page,
                size = size,
            ).toDomainResult { response ->
                response.toVideo()
            }
        }

        private inline fun <T, R> RemoteResponse<T>.toDomainResult(
            mapper: (T) -> R,
        ): DomainResult<R> {
            if (!isSuccessful || body == null) {
                return DomainResult.Fail(code, message)
            }

            return DomainResult.Success(mapper(body))
        }
    }
