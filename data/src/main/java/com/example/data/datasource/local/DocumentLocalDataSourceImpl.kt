package com.example.data.datasource.local

import com.example.data.datasource.local.dao.SearchingDao
import com.example.data.datasource.local.entity.DocumentEntity
import javax.inject.Inject

class DocumentLocalDataSourceImpl @Inject constructor(
    private val searchingDao: SearchingDao
) : DocumentLocalDataSource {
    override suspend fun getSavedDocumentEntityList(): List<DocumentEntity> {
        return searchingDao.getSavedSearchingData()
    }

    override suspend fun insertDocumentEntity(documentEntity: DocumentEntity) {
        searchingDao.insertSearchingEntity(documentEntity)
    }
}