package com.example.domain.usecase

import com.example.domain.entity.ImageEntity
import com.example.domain.entity.VideoEntity

interface MediaSearchResultUseCase {
    suspend fun getImageResult(
        query: String,
        page: Int,
        size: Int
    ): ImageEntity

    suspend fun getVideoResult(
        query: String,
        page: Int,
        size: Int
    ): VideoEntity
}