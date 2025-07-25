package com.example.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.datasource.local.dao.SearchingDao
import com.example.data.datasource.local.entity.SearchingEntity

@Database(entities = [SearchingEntity::class], version = 1)
abstract class SearchingResultDataBase : RoomDatabase() {
    abstract fun searchingDao() : SearchingDao
}