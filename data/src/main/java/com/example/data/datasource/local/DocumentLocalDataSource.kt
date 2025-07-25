package com.example.data.datasource.local

import com.example.data.datasource.local.entity.DocumentEntity

interface DocumentLocalDataSource {
    suspend fun getSavedDocumentEntityList() : List<DocumentEntity>
    suspend fun insertDocumentEntity(documentEntity: DocumentEntity)
}