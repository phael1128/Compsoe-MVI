package com.example.domain.mapping

import com.example.data.datasource.remote.response.ResDocument
import com.example.domain.entity.DocumentEntity
import com.example.domain.entity.SearchingViewType

fun ResDocument.toDocumentEntity() = DocumentEntity(
    collection = collection,
    thumbnailUrl = thumbnailUrl,
    imageUrl = imageUrl,
    width = width,
    height = height,
    displaySiteName = displaySiteName,
    docUrl = doc_url,
    author = author,
    playTime = playTime,
    thumbnail = thumbnail,
    title = title,
    url = url,
    datetime = datetime
)

fun ArrayList<ResDocument>.toDocumentEntityList(
    searchingViewType: SearchingViewType
): ArrayList<DocumentEntity> = arrayListOf<DocumentEntity>().apply {
    this@toDocumentEntityList.forEach {
        add(
            it.toDocumentEntity().apply {
                this.searchingViewType = searchingViewType
            }
        )
    }
}