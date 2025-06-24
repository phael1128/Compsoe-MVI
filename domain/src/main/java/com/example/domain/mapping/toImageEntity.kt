package com.example.domain.mapping

import com.example.data.datasource.remote.response.ResImage
import com.example.domain.entity.ImageEntity

fun ResImage.toImageEntity() = ImageEntity(
    meta = this.meta.toMetaEntity(),
    documents = this.documents.toDocumentEntityList()
)