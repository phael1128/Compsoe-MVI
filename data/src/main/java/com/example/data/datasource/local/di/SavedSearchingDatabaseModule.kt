package com.example.data.datasource.local.di

import android.content.Context
import androidx.room.Room
import com.example.data.datasource.local.database.SearchingResultDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedSearchingDatabaseModule {

    @Provides
    @Singleton
    fun provideSearchingResultDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        SearchingResultDataBase::class.java,
        "searching-result-database"
    ).build()

    @Provides
    @Singleton
    fun provideSearchingDao(
        searchingResultDataBase: SearchingResultDataBase
    ) = searchingResultDataBase.searchingDao()
}