package com.example.data.datasource.remote.network

import com.example.data.datasource.remote.response.ResImage
import com.example.data.datasource.remote.response.ResVideo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkAPI {
    // 이미지 검색
    @GET("v2/search/image")
    suspend fun searchImageResult(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ResImage>

    // 비디오 검색
    @GET("/v2/search/vclip")
    suspend fun searchVideoResult(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ResVideo>
}
