package com.example.domain.mapping

import com.example.data.datasource.remote.response.ResVideo
import com.example.domain.entity.VideoEntity

fun ResVideo.toVideoEntity() = VideoEntity(
    meta = this.meta.toMetaEntity(),
    documents = this.documents.toDocumentEntityList()
)