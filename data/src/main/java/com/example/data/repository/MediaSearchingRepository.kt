package com.example.data.repository

import com.example.data.datasource.remote.response.ResImage
import com.example.data.datasource.remote.response.ResVideo
import com.example.data.model.ResponseResult

interface MediaSearchingRepository {
    suspend fun getImageResult(
        query: String,
        page: Int,
        size: Int,
    ): ResponseResult<ResImage>

    suspend fun getVideoResult(
        query: String,
        page: Int,
        size: Int,
    ): ResponseResult<ResVideo>
}
