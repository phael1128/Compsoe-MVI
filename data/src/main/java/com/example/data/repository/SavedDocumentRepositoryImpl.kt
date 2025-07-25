package com.example.data.repository

import com.example.data.datasource.local.DocumentLocalDataSource
import com.example.data.datasource.local.entity.DocumentEntity
import javax.inject.Inject

class SavedDocumentRepositoryImpl @Inject constructor(
    private val documentLocalDataSource: DocumentLocalDataSource
) : SavedDocumentRepository {
    override suspend fun getSavedDocumentEntityList(): List<DocumentEntity> {
        return documentLocalDataSource.getSavedDocumentEntityList()
    }

    override suspend fun insertDocumentEntity(documentEntity: DocumentEntity) {
        documentLocalDataSource.insertDocumentEntity(documentEntity)
    }
}