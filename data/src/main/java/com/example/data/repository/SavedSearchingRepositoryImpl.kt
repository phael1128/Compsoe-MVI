package com.example.data.repository

import com.example.data.datasource.local.SearchingLocalDataSource
import com.example.data.datasource.local.entity.SearchingEntity
import javax.inject.Inject

class SavedSearchingRepositoryImpl @Inject constructor(
    private val searchingLocalDataSource: SearchingLocalDataSource
) : SavedSearchingRepository {
    override suspend fun getSavedSearchingEntityList(): List<SearchingEntity> {
        return searchingLocalDataSource.getSavedSearchingEntityList()
    }

    override suspend fun insertSearchingEntity(searchingEntity: SearchingEntity) {
        searchingLocalDataSource.insertSearchingEntity(searchingEntity)
    }
}