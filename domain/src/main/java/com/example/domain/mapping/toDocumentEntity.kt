package com.example.domain.mapping

import com.example.data.datasource.local.entity.DocumentEntity
import com.example.data.datasource.remote.response.ResDocument
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType

fun ResDocument.toDocumentEntity() = Document(
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
    searchingViewType: SearchingViewType,
    callback: (url: String?) -> Boolean
): ArrayList<Document> = arrayListOf<Document>().apply {
    this@toDocumentEntityList.forEach {
        add(
            it.toDocumentEntity().apply {
                this.searchingViewType = searchingViewType
                this.isSaveButtonVisible = callback(if (searchingViewType == SearchingViewType.Image) docUrl else url)
            }
        )
    }
}

fun DocumentEntity.toDocumentEntity() = Document(
    collection = collection,
    thumbnailUrl = thumbnailUrl,
    imageUrl = imageUrl,
    width = width,
    height = height,
    displaySiteName = displaySiteName,
    docUrl = docUrl,
    author = author,
    playTime = playTime,
    thumbnail = thumbnail,
    title = title,
    url = url,
    datetime = datetime
)

fun List<DocumentEntity>.toDocumentEntity(): List<Document> = mutableListOf<Document>().apply {
    this@toDocumentEntity.forEach {
        add(
            it.toDocumentEntity().apply {
                searchingViewType = it.title.takeIf {
                    !docUrl.isNullOrEmpty()
                }?.let {
                    SearchingViewType.Image
                } ?: SearchingViewType.Video
            }
        )
    }
}