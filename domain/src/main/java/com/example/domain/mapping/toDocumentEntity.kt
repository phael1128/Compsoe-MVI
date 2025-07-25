package com.example.domain.mapping

import com.example.data.datasource.local.entity.SearchingEntity
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
    searchingViewType: SearchingViewType,
    callback: (url: String?) -> Boolean
): ArrayList<DocumentEntity> = arrayListOf<DocumentEntity>().apply {
    this@toDocumentEntityList.forEach {
        add(
            it.toDocumentEntity().apply {
                this.searchingViewType = searchingViewType
                this.isSaveButtonVisible = callback(if (searchingViewType == SearchingViewType.Image) docUrl else url)
            }
        )
    }
}

fun SearchingEntity.toDocumentEntity() = DocumentEntity(
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

fun List<SearchingEntity>.toDocumentEntity(): List<DocumentEntity> = mutableListOf<DocumentEntity>().apply {
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