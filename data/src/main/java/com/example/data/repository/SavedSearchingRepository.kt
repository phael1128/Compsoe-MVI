package com.example.data.repository

import com.example.data.datasource.local.entity.SearchingEntity

interface SavedSearchingRepository {
    suspend fun getSavedSearchingEntityList() : List<SearchingEntity>
    suspend fun insertSearchingEntity(searchingEntity: SearchingEntity)
}