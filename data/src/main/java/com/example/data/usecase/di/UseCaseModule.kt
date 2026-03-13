package com.example.data.usecase.di

import com.example.domain.repository.MediaSearchingRepository
import com.example.domain.repository.SavedDocumentRepository
import com.example.domain.usecase.MediaSearchResultUseCase
import com.example.domain.usecase.SavedDocumentResultUseCase
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
    fun provideSavedDocumentResultUseCase(
        savedDocumentRepository: SavedDocumentRepository,
    ): SavedDocumentResultUseCase = SavedDocumentResultUseCase(savedDocumentRepository)

    @Provides
    @Singleton
    fun provideMediaSearchResultUseCase(
        mediaSearchingRepository: MediaSearchingRepository,
        savedDocumentResultUseCase: SavedDocumentResultUseCase,
    ): MediaSearchResultUseCase = MediaSearchResultUseCase(
        mediaSearchingRepository = mediaSearchingRepository,
        savedDocumentResultUseCase = savedDocumentResultUseCase,
    )
}
