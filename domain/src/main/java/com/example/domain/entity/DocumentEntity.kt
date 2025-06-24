package com.example.domain.entity

data class DocumentEntity(
    val collection: String?,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val width: Long?,
    val height: Long?,
    val displaySiteName: String?,
    val doc_url: String?,
    val author: String?,
    val playTime: Int?,
    val thumbnail: String?,
    val title: String?,
    val url: String?,
    val datetime: String,
    var viewType: Int
)
