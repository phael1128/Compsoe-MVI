package com.example.domain.usecase

import com.example.domain.entity.Document

interface SavedDocumentResultUseCase {
    suspend fun getSavedDocumentEntity(): List<Document>
    suspend fun insertDocumentEntity(document: Document)
}