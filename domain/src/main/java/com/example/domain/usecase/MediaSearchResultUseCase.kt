package com.example.domain.usecase

import com.example.domain.entity.Document

interface MediaSearchResultUseCase {
    suspend operator fun invoke(query: String): ArrayList<Document>

    fun getLastKeyword(): String

    fun setKeyWord(keyword: String)
}
