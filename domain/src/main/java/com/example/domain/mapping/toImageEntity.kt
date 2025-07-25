package com.example.domain.mapping

import com.example.data.datasource.remote.response.ResImage
import com.example.domain.entity.Image
import com.example.domain.entity.SearchingViewType

fun ResImage.toImageEntity(
    savedImageUrlList: List<String>
) = Image(
    meta = this.meta.toMetaEntity(),
    documents = this.documents.toDocumentEntityList(
        searchingViewType = SearchingViewType.Image,
        callback = { docUrl ->
            savedImageUrlList.contains(docUrl)
        }
    )
)