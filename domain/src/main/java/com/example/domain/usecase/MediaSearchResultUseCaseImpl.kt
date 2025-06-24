package com.example.domain.usecase

import com.example.data.repository.MediaSearchingRepository
import com.example.domain.entity.ImageEntity
import com.example.domain.entity.VideoEntity
import com.example.domain.mapping.toImageEntity
import com.example.domain.mapping.toVideoEntity
import javax.inject.Inject

class MediaSearchResultUseCaseImpl @Inject constructor(
    private val mediaSearchingRepository: MediaSearchingRepository
) : MediaSearchResultUseCase {
    override suspend fun getImageResult(query: String, page: Int, size: Int): ImageEntity {
        return mediaSearchingRepository.getImageResult(
            query,
            page,
            size
        ).toImageEntity()
    }

    override suspend fun getVideoResult(query: String, page: Int, size: Int): VideoEntity {
        return mediaSearchingRepository.getVideoResult(
            query,
            page,
            size
        ).toVideoEntity()
    }
}