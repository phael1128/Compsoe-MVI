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
        private var imagePageCount = FIRST_PAGE
        private var videoPageCount = FIRST_PAGE

        suspend operator fun invoke(query: String): ArrayList<Document> {
            if (isNewKeyword(query)) {
                setKeyWord(query)
                initializeQueryData()
            }

            val documentList = ArrayList<Document>()
            val savedDocuments = savedDocumentResultUseCase.getSavedDocumentEntity()
            val savedImageDocUrlSet = savedDocuments.mapNotNullTo(hashSetOf()) { it.docUrl }
            val savedVideoUrlSet = savedDocuments.mapNotNullTo(hashSetOf()) { it.url }

            if (!isLastImageResult) {
                getImageResult(
                    query,
                    imagePageCount,
                    savedImageDocUrlSet,
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
                    savedVideoUrlSet,
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
            savedImageDocUrlSet: Set<String>,
            onSuccessCallback: (Image) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getImageResult(query, page, PAGE_SIZE)) {
                is DomainResult.Success -> {
                    result.body.documents.forEach { document ->
                        document.isSaved = document.docUrl in savedImageDocUrlSet
                    }
                    onSuccessCallback.invoke(result.body)
                }

                is DomainResult.Fail -> Unit
            }
        }

        private suspend fun getVideoResult(
            query: String,
            page: Int,
            savedVideoUrlSet: Set<String>,
            onSuccessCallback: (Video) -> Unit,
        ) {
            when (val result = mediaSearchingRepository.getVideoResult(query, page, PAGE_SIZE)) {
                is DomainResult.Success -> {
                    result.body.documents.forEach { document ->
                        document.isSaved = document.url in savedVideoUrlSet
                    }
                    onSuccessCallback.invoke(result.body)
                }

                is DomainResult.Fail -> Unit
            }
        }

        private fun initializeQueryData() {
            imagePageCount = FIRST_PAGE
            isLastImageResult = false
            videoPageCount = FIRST_PAGE
            isLastVideoResult = false
        }

        private fun isNewKeyword(query: String) = query != lastKeyword

        companion object {
            private const val FIRST_PAGE = 1
            private const val PAGE_SIZE = 30
        }
    }
