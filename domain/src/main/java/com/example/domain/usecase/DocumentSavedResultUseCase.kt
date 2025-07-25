package com.example.domain.usecase

import com.example.domain.entity.DocumentEntity

interface DocumentSavedResultUseCase {
    suspend fun getSavedDocumentEntity(): List<DocumentEntity>
    suspend fun insertDocumentEntity(documentEntity: DocumentEntity)
}