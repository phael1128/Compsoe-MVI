package com.example.domain.usecase

import com.example.domain.entity.Document
import com.example.domain.repository.SavedDocumentRepository

class SavedDocumentResultUseCase
    constructor(
        private val savedDocumentRepository: SavedDocumentRepository,
    ) {
        suspend fun getSavedDocumentEntity(): List<Document> = savedDocumentRepository.getSavedDocumentEntityList()

        suspend fun insertDocumentEntity(document: Document) {
            savedDocumentRepository.insertDocumentEntity(document)
        }
    }
