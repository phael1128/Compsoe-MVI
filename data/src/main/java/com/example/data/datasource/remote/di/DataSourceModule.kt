package com.example.data.datasource.remote.di

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.datasource.remote.MediaSearchingRemoteDataSourceImpl
import com.example.data.datasource.remote.network.HttpUrlConnectionClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideMediaSearchingRemoteDataSource(
        httpUrlConnectionClient: HttpUrlConnectionClient,
    ): MediaSearchingRemoteDataSource = MediaSearchingRemoteDataSourceImpl(httpUrlConnectionClient)
}
