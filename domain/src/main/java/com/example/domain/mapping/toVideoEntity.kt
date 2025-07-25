package com.example.domain.mapping

import com.example.data.datasource.remote.response.ResVideo
import com.example.domain.entity.SearchingViewType
import com.example.domain.entity.Video

fun ResVideo.toVideoEntity(
    savedVideoUrlList: List<String>
) = Video(
    meta = this.meta.toMetaEntity(),
    documents = this.documents.toDocumentEntityList(
        searchingViewType = SearchingViewType.Video,
        callback = { url ->
            savedVideoUrlList.contains(url)
        }
    )
)