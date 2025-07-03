package com.example.domain.usecase

import android.util.Log
import com.example.data.model.ResponseResult
import com.example.data.repository.MediaSearchingRepository
import com.example.domain.entity.DocumentEntity
import com.example.domain.entity.ImageEntity
import com.example.domain.entity.VideoEntity
import com.example.domain.mapping.toImageEntity
import com.example.domain.mapping.toVideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaSearchResultUseCaseImpl
    @Inject
    constructor(
        private val mediaSearchingRepository: MediaSearchingRepository,
    ) : MediaSearchResultUseCase {
        private var isLastImageResult = false
        private var isLastVideoResult = false
        private var lastKeyword = ""
        private var imagePageCount = 1
        private var videoPageCount = 1

        override suspend fun invoke(query: String): ArrayList<DocumentEntity> =
            withContext(Dispatchers.IO) {
                if (isNewKeyword(query)) {
                    setKeyWord(query)
                    initializePageCount()
                }

                val documentEntityList = ArrayList<DocumentEntity>()

                if (!isLastImageResult) {
                    getImageResult(
                        query,
                        imagePageCount,
                        onSuccessCallback = { imageEntity ->
                            isLastImageResult = imageEntity.meta.is_end
                            documentEntityList.addAll(imageEntity.documents)
                            imagePageCount++
                        },
                    )
                }

                if (!isLastVideoResult) {
                    getVideoResult(
                        query,
                        videoPageCount,
                        onSuccessCallback = { videoEntity ->
                            isLastVideoResult = videoEntity.meta.is_end
                            documentEntityList.addAll(videoEntity.documents)
                            videoPageCount++
                        },
                    )
                }

                documentEntityList.sortByDescending { it.datetime }
                return@withContext documentEntityList
            }

        override fun getLastKeyword() = lastKeyword

        override fun setKeyWord(keyword: String) {
            lastKeyword = keyword
        }

        private suspend fun getImageResult(
            query: String,
            page: Int,
            onSuccessCallback: (ImageEntity) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getImageResult(query, page, PAGE_SIZE)) {
                is ResponseResult.Success -> {
                    onSuccessCallback.invoke(result.body.toImageEntity())
                }
                is ResponseResult.Fail -> {
                    Log.d("phael", "error code : ${result.errorCode} error message : ${result.errorMessage}")
                }
            }
        }

        private suspend fun getVideoResult(
            query: String,
            page: Int,
            onSuccessCallback: (VideoEntity) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getVideoResult(query, page, PAGE_SIZE)) {
                is ResponseResult.Success -> {
                    onSuccessCallback.invoke(result.body.toVideoEntity())
                }
                is ResponseResult.Fail -> {
                    Log.d("phael", "error code : ${result.errorCode} error message : ${result.errorMessage}")
                }
            }
        }

        private fun initializePageCount() {
            imagePageCount = 0
            videoPageCount = 0
        }

        private fun isNewKeyword(query: String) = query != lastKeyword

        companion object {
            private const val PAGE_SIZE = 30
        }
    }
