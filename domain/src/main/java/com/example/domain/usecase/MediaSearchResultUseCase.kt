package com.example.domain.usecase

import com.example.domain.entity.DocumentEntity

interface MediaSearchResultUseCase {
    suspend operator fun invoke(
        query: String,
        page: Int,
        size: Int,
    ): ArrayList<DocumentEntity>

    fun getLastKeyword(): String

    fun setKeyWord(keyword: String)
}
