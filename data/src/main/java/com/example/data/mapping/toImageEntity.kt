package com.example.data.mapping

import com.example.data.datasource.remote.response.ResImage
import com.example.domain.entity.Image
import com.example.domain.entity.SearchingViewType

fun ResImage.toImage() = Image(
    meta = this.meta.toMeta(),
    documents = this.documents.toDocumentList(
        searchingViewType = SearchingViewType.Image,
    )
)
