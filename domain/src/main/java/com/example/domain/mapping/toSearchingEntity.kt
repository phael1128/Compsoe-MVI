package com.example.domain.mapping

import com.example.data.datasource.local.entity.DocumentEntity
import com.example.domain.entity.Document

fun Document.toSearchingEntity() : DocumentEntity = DocumentEntity(
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