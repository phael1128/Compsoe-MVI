package com.example.domain.repository

import com.example.domain.entity.Image
import com.example.domain.entity.Video
import com.example.domain.model.DomainResult

interface MediaSearchingRepository {
    suspend fun getImageResult(
        query: String,
        page: Int,
        size: Int,
    ): DomainResult<Image>

    suspend fun getVideoResult(
        query: String,
        page: Int,
        size: Int,
    ): DomainResult<Video>
}
