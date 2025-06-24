package com.example.domain.mapping

import com.example.data.datasource.remote.response.ResDocument
import com.example.domain.entity.DocumentEntity

fun ResDocument.toDocumentEntity() = DocumentEntity(
    collection = collection,
    thumbnailUrl = thumbnailUrl,
    imageUrl = imageUrl,
    width = width,
    height = height,
    displaySiteName = displaySiteName,
    doc_url = doc_url,
    author = author,
    playTime = playTime,
    thumbnail = thumbnail,
    title = title,
    url = url,
    datetime = datetime,
    viewType = viewType
)

fun ArrayList<ResDocument>.toDocumentEntityList(): ArrayList<DocumentEntity> = arrayListOf<DocumentEntity>().apply {
    this@toDocumentEntityList.forEach {
        add(it.toDocumentEntity())
    }
}