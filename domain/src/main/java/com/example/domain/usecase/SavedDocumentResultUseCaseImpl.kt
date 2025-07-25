package com.example.domain.usecase

import com.example.data.repository.SavedDocumentRepository
import com.example.domain.entity.Document
import com.example.domain.mapping.toDocumentEntity
import com.example.domain.mapping.toSearchingEntity
import javax.inject.Inject

class SavedDocumentResultUseCaseImpl @Inject constructor(
    private val savedDocumentRepository: SavedDocumentRepository
) : SavedDocumentResultUseCase {
    override suspend fun getSavedDocumentEntity(): List<Document> {
        return savedDocumentRepository.getSavedDocumentEntityList().toDocumentEntity()
    }

    override suspend fun insertDocumentEntity(document: Document) {
        savedDocumentRepository.insertDocumentEntity(document.toSearchingEntity())
    }
}