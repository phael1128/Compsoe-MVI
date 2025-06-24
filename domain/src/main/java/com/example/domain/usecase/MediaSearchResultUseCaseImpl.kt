package com.example.domain.usecase

import com.example.data.repository.MediaSearchingRepository
import com.example.domain.entity.DocumentEntity
import com.example.domain.entity.ImageEntity
import com.example.domain.entity.VideoEntity
import com.example.domain.mapping.toImageEntity
import com.example.domain.mapping.toVideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaSearchResultUseCaseImpl @Inject constructor(
    private val mediaSearchingRepository: MediaSearchingRepository
) : MediaSearchResultUseCase {

    override suspend fun invoke(
        query: String, page: Int, size: Int
    ): ArrayList<DocumentEntity> = withContext(Dispatchers.IO) {
        val imageResult = getImageResult(query, page, size)
        val videoResult = getVideoResult(query, page, size)

        val documentEntityList = ArrayList<DocumentEntity>().apply {
            addAll(imageResult.documents)
            addAll(videoResult.documents)
        }
        documentEntityList.sortByDescending { it.datetime }

        return@withContext documentEntityList
    }

    private suspend fun getImageResult(query: String, page: Int, size: Int): ImageEntity {
        return mediaSearchingRepository.getImageResult(
            query,
            page,
            size
        ).toImageEntity()
    }

    private suspend fun getVideoResult(query: String, page: Int, size: Int): VideoEntity {
        return mediaSearchingRepository.getVideoResult(
            query,
            page,
            size
        ).toVideoEntity()
    }
}