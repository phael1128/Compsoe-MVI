package com.example.domain.usecase

import com.example.domain.entity.Document
import com.example.domain.entity.Image
import com.example.domain.entity.Video
import com.example.domain.model.DomainResult
import com.example.domain.repository.MediaSearchingRepository

class MediaSearchResultUseCase
    constructor(
        private val mediaSearchingRepository: MediaSearchingRepository,
        private val savedDocumentResultUseCase: SavedDocumentResultUseCase,
    ) {
        private var isLastImageResult = false
        private var isLastVideoResult = false
        private var lastKeyword = ""
        private var imagePageCount = 1
        private var videoPageCount = 1

        suspend operator fun invoke(query: String): ArrayList<Document> {
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
            return documentList
        }

        fun getLastKeyword() = lastKeyword

        fun setKeyWord(keyword: String) {
            lastKeyword = keyword
        }

        private suspend fun getImageResult(
            query: String,
            page: Int,
            onSuccessCallback: (Image) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getImageResult(query, page, PAGE_SIZE)) {
                is DomainResult.Success -> {
                    val savedDocUrlList = getSavedImageDocUrlLinkList().filterNotNull()
                    result.body.documents.forEach { document ->
                        document.isSaveButtonVisible = savedDocUrlList.contains(document.docUrl)
                    }
                    onSuccessCallback.invoke(result.body)
                }

                is DomainResult.Fail -> {
                    Unit
                }
            }
        }

        private suspend fun getVideoResult(
            query: String,
            page: Int,
            onSuccessCallback: (Video) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getVideoResult(query, page, PAGE_SIZE)) {
                is DomainResult.Success -> {
                    val savedVideoUrlList = getSavedVideoUrlList().filterNotNull()
                    result.body.documents.forEach { document ->
                        document.isSaveButtonVisible = savedVideoUrlList.contains(document.url)
                    }
                    onSuccessCallback.invoke(result.body)
                }

                is DomainResult.Fail -> {
                    Unit
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
