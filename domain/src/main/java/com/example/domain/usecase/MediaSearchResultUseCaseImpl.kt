package com.example.domain.usecase

import android.util.Log
import com.example.data.model.ResponseResult
import com.example.data.repository.MediaSearchingRepository
import com.example.domain.entity.Document
import com.example.domain.entity.Image
import com.example.domain.entity.Video
import com.example.domain.mapping.toImageEntity
import com.example.domain.mapping.toVideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaSearchResultUseCaseImpl
    @Inject
    constructor(
        private val mediaSearchingRepository: MediaSearchingRepository,
        private val savedDocumentResultUseCase: SavedDocumentResultUseCase
    ) : MediaSearchResultUseCase {
        private var isLastImageResult = false
        private var isLastVideoResult = false
        private var lastKeyword = ""
        private var imagePageCount = 1
        private var videoPageCount = 1

        override suspend fun invoke(query: String): ArrayList<Document> =
            withContext(Dispatchers.IO) {
                if (isNewKeyword(query)) {
                    setKeyWord(query)
                    initializeQueryData()
                }

                val documentList = ArrayList<Document>()

                if (!isLastImageResult) {
                    getImageResult(
                        query,
                        imagePageCount,
                        onSuccessCallback = { imageEntity ->
                            isLastImageResult = imageEntity.meta.is_end
                            documentList.addAll(imageEntity.documents)
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
                            documentList.addAll(videoEntity.documents)
                            videoPageCount++
                        },
                    )
                }
                documentList.sortByDescending { it.datetime }
                return@withContext documentList
            }

        override fun getLastKeyword() = lastKeyword

        override fun setKeyWord(keyword: String) {
            lastKeyword = keyword
        }

        private suspend fun getImageResult(
            query: String,
            page: Int,
            onSuccessCallback: (Image) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getImageResult(query, page, PAGE_SIZE)) {
                is ResponseResult.Success -> {
                    val imageResult = getSavedImageDocUrlLinkList().filterNotNull().let { savedImageDocUrlList ->
                        result.body.toImageEntity(savedImageDocUrlList)
                    }
                    onSuccessCallback.invoke(imageResult)
                }
                is ResponseResult.Fail -> {
                    Log.d("phael", "error code : ${result.errorCode} error message : ${result.errorMessage}")
                }
            }
        }

        private suspend fun getVideoResult(
            query: String,
            page: Int,
            onSuccessCallback: (Video) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getVideoResult(query, page, PAGE_SIZE)) {
                is ResponseResult.Success -> {
                    val videoResult = getSavedVideoUrlList().filterNotNull().let { savedVideoUrlList ->
                        result.body.toVideoEntity(savedVideoUrlList)
                    }
                    onSuccessCallback.invoke(videoResult)
                }
                is ResponseResult.Fail -> {
                    Log.d("phael", "error code : ${result.errorCode} error message : ${result.errorMessage}")
                }
            }
        }

        private fun initializeQueryData() {
            imagePageCount = 0
            isLastImageResult = false
            videoPageCount = 0
            isLastVideoResult = false
        }

        private fun isNewKeyword(query: String) = query != lastKeyword

        private suspend fun getSavedImageDocUrlLinkList() = savedDocumentResultUseCase.getSavedDocumentEntity().map { it.docUrl }

        private suspend fun getSavedVideoUrlList() = savedDocumentResultUseCase.getSavedDocumentEntity().map { it.url }

        companion object {
            private const val PAGE_SIZE = 30
        }
    }
