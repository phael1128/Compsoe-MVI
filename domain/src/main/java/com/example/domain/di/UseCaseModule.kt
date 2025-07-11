package com.example.domain.di

import com.example.data.repository.MediaSearchingRepository
import com.example.domain.usecase.MediaSearchResultUseCase
import com.example.domain.usecase.MediaSearchResultUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideMediaSearchResultUseCase(
        mediaSearchingRepository: MediaSearchingRepository
    ): MediaSearchResultUseCase = MediaSearchResultUseCaseImpl(mediaSearchingRepository)
}