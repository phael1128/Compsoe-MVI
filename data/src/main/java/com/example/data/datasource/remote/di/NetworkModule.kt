package com.example.data.datasource.remote.di

import android.content.Context
import android.util.Log
import com.example.data.datasource.remote.network.HeaderInterceptor
import com.example.data.datasource.remote.network.NetworkAPI
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://dapi.kakao.com/"

    @Singleton
    @Provides
    fun provideGsonDateFormatter(): Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor.Logger { message ->
            Log.d("Intercepter Message : ", message)
        }
        return HttpLoggingInterceptor(logger).apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(
        context: Context
    ) = HeaderInterceptor(context)

    @Singleton
    @Provides
    fun provideOkhttpClient(
        logger: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(headerInterceptor)
            addInterceptor(logger)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NetworkAPI = retrofit.create(NetworkAPI::class.java)

}