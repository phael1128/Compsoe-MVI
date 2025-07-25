package com.example.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.datasource.local.dao.SearchingDao
import com.example.data.datasource.local.entity.DocumentEntity

@Database(entities = [DocumentEntity::class], version = 1)
abstract class SearchingResultDataBase : RoomDatabase() {
    abstract fun searchingDao() : SearchingDao
}