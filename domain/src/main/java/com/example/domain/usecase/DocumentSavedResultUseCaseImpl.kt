package com.example.domain.usecase

import com.example.data.repository.SavedSearchingRepository
import com.example.domain.entity.DocumentEntity
import com.example.domain.mapping.toDocumentEntity
import com.example.domain.mapping.toSearchingEntity
import javax.inject.Inject

class DocumentSavedResultUseCaseImpl @Inject constructor(
    private val savedSearchingRepository: SavedSearchingRepository
) : DocumentSavedResultUseCase {
    override suspend fun getSavedDocumentEntity(): List<DocumentEntity> {
        return savedSearchingRepository.getSavedSearchingEntityList().toDocumentEntity()
    }

    override suspend fun insertDocumentEntity(documentEntity: DocumentEntity) {
        savedSearchingRepository.insertSearchingEntity(documentEntity.toSearchingEntity())
    }
}