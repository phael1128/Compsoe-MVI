package com.example.domain.repository

import com.example.domain.entity.Document

interface SavedDocumentRepository {
    suspend fun getSavedDocumentEntityList(): List<Document>
    suspend fun insertDocumentEntity(document: Document)
}
