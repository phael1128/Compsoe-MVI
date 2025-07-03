package com.example.data.datasource.remote

import com.example.data.datasource.remote.response.ResImage
import com.example.data.datasource.remote.response.ResVideo
import retrofit2.Response

interface MediaSearchingRemoteDataSource {
    suspend fun searchImageResult(
        query: String,
        page: Int,
        size: Int,
    ): Response<ResImage>

    suspend fun searchVideoResult(
        query: String,
        page: Int,
        size: Int,
    ): Response<ResVideo>
}
