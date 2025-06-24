package com.example.data.repository.di

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.repository.MediaSearchingRepository
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
    ) = MediaSearchingRepository(mediaSearchingRemoteDataSource)
}