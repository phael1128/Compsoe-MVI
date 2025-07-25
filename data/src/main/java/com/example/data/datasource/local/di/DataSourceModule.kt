package com.example.data.datasource.local.di

import com.example.data.datasource.local.DocumentLocalDataSource
import com.example.data.datasource.local.DocumentLocalDataSourceImpl
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
    ): DocumentLocalDataSource = DocumentLocalDataSourceImpl(searchingDao)
}