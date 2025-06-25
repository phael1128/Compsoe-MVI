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
    private var isLastImageResult = false
    private var isLastVideoResult = false
    private var lastKeyword = ""

    override suspend fun invoke(
        query: String,
        page: Int,
        size: Int
    ): ArrayList<DocumentEntity> = withContext(Dispatchers.IO) {
        lastKeyword = query

        val imageResult = if (!isLastImageResult) {
            val imageResult = getImageResult(query, page, size)
            isLastImageResult = imageResult.meta.is_end
            imageResult.documents
        } else {
            emptyList<DocumentEntity>()
        }

        val videoResult = if (!isLastVideoResult) {
            val videoResult = getVideoResult(query, page, size)
            isLastVideoResult = videoResult.meta.is_end
            videoResult.documents
        } else {
            emptyList<DocumentEntity>()
        }

        val documentEntityList = ArrayList<DocumentEntity>().apply {
            addAll(imageResult)
            addAll(videoResult)
        }
        documentEntityList.sortByDescending { it.datetime }

        return@withContext documentEntityList
    }

    override fun getLastKeyword() = lastKeyword

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