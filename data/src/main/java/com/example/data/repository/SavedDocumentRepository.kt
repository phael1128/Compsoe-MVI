package com.example.data.repository

import com.example.data.datasource.local.entity.DocumentEntity

interface SavedDocumentRepository {
    suspend fun getSavedDocumentEntityList() : List<DocumentEntity>
    suspend fun insertDocumentEntity(documentEntity: DocumentEntity)
}