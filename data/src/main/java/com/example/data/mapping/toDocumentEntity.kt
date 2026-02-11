package com.example.data.mapping

import com.example.data.datasource.local.entity.DocumentEntity
import com.example.data.datasource.remote.response.ResDocument
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType

fun ResDocument.toDocument() = Document(
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

fun ArrayList<ResDocument>.toDocumentList(
    searchingViewType: SearchingViewType,
): ArrayList<Document> = arrayListOf<Document>().apply {
    this@toDocumentList.forEach {
        add(
            it.toDocument().apply {
                this.searchingViewType = searchingViewType
            }
        )
    }
}

fun DocumentEntity.toDocument() = Document(
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

fun List<DocumentEntity>.toDocumentList(): List<Document> = map { entity ->
    entity.toDocument().apply {
        searchingViewType = entity.title.takeIf {
            !entity.docUrl.isNullOrEmpty()
        }?.let {
            SearchingViewType.Image
        } ?: SearchingViewType.Video
    }
}

fun Document.toDocumentEntity() = DocumentEntity(
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
