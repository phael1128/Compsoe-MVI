package com.example.data.datasource.local

import com.example.data.datasource.local.entity.SearchingEntity

interface SearchingLocalDataSource {
    suspend fun getSavedSearchingEntityList() : List<SearchingEntity>
    suspend fun insertSearchingEntity(searchingEntity: SearchingEntity)
}