package com.example.data.datasource.local

import com.example.data.datasource.local.dao.SearchingDao
import com.example.data.datasource.local.entity.SearchingEntity
import javax.inject.Inject

class SearchingLocalDataSourceImpl @Inject constructor(
    private val searchingDao: SearchingDao
) : SearchingLocalDataSource {
    override suspend fun getSavedSearchingEntityList(): List<SearchingEntity> {
        return searchingDao.getSavedSearchingData()
    }

    override suspend fun insertSearchingEntity(searchingEntity: SearchingEntity) {
        searchingDao.insertSearchingEntity(searchingEntity)
    }
}