package com.example.data.repository

import com.example.data.datasource.local.DocumentLocalDataSource
import com.example.data.mapping.toDocumentEntity
import com.example.data.mapping.toDocumentList
import com.example.domain.entity.Document
import com.example.domain.repository.SavedDocumentRepository
import javax.inject.Inject

class SavedDocumentRepositoryImpl @Inject constructor(
    private val documentLocalDataSource: DocumentLocalDataSource
) : SavedDocumentRepository {
    override suspend fun getSavedDocumentEntityList(): List<Document> {
        return documentLocalDataSource.getSavedDocumentEntityList().toDocumentList()
    }

    override suspend fun insertDocumentEntity(document: Document) {
        documentLocalDataSource.insertDocumentEntity(document.toDocumentEntity())
    }
}
