package com.example.domain.entity

data class Document(
    val collection: String?,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val width: Long?,
    val height: Long?,
    val displaySiteName: String?,
    val docUrl: String?,
    val author: String?,
    val playTime: Int?,
    val thumbnail: String?,
    val title: String?,
    val url: String?,
    val datetime: String,
    var searchingViewType: SearchingViewType? = null,
    var isSaveButtonVisible: Boolean = false
)

fun fakeDocument() = Document(
    collection = "fake collection",
    thumbnailUrl = "fake thumbnailUrl",
    imageUrl = "fake imageUrl",
    width = 1L,
    height = 1L,
    displaySiteName = "fake displaySiteName",
    docUrl = "fake docUrl",
    author = "fake author",
    playTime = 30,
    thumbnail = "fake thumbnail",
    title = "fake title",
    url = "fake url",
    datetime = "",
)