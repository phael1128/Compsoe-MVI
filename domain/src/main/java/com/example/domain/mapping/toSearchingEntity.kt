package com.example.domain.mapping

import com.example.data.datasource.local.entity.SearchingEntity
import com.example.domain.entity.DocumentEntity

fun DocumentEntity.toSearchingEntity() : SearchingEntity = SearchingEntity(
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