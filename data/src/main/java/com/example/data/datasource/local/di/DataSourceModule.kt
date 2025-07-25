package com.example.data.datasource.local.di

import com.example.data.datasource.local.SearchingLocalDataSource
import com.example.data.datasource.local.SearchingLocalDataSourceImpl
import com.example.data.datasource.local.dao.SearchingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun provideSearchingDataSource(
        searchingDao: SearchingDao
    ): SearchingLocalDataSource = SearchingLocalDataSourceImpl(searchingDao)
}