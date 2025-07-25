package com.example.data.repository.di

import com.example.data.datasource.local.DocumentLocalDataSource
import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.repository.MediaSearchingRepository
import com.example.data.repository.MediaSearchingRepositoryImpl
import com.example.data.repository.SavedDocumentRepository
import com.example.data.repository.SavedDocumentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMediaSearchingRepository(
        mediaSearchingRemoteDataSource: MediaSearchingRemoteDataSource
    ): MediaSearchingRepository = MediaSearchingRepositoryImpl(mediaSearchingRemoteDataSource)

    @Provides
    @Singleton
    fun provideSavedSearchingRepository(
        documentLocalDataSource: DocumentLocalDataSource
    ): SavedDocumentRepository = SavedDocumentRepositoryImpl(documentLocalDataSource)
}
