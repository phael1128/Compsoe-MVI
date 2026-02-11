package com.example.domain.usecase

import com.example.domain.entity.Document
import com.example.domain.repository.SavedDocumentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedDocumentResultUseCaseImpl @Inject constructor(
    private val savedDocumentRepository: SavedDocumentRepository
) : SavedDocumentResultUseCase {
    override suspend fun getSavedDocumentEntity(): List<Document> {
        return withContext(Dispatchers.IO) {
            savedDocumentRepository.getSavedDocumentEntityList()
        }
    }

    override suspend fun insertDocumentEntity(document: Document) {
        savedDocumentRepository.insertDocumentEntity(document)
    }
}
