package com.example.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.datasource.local.entity.SearchingEntity

@Dao
interface SearchingDao {
    @Query("SELECT * FROM searching_table")
    suspend fun getSavedSearchingData() : List<SearchingEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchingEntity(searchingEntity: SearchingEntity)
}