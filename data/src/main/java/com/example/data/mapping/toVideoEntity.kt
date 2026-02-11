package com.example.data.mapping

import com.example.data.datasource.remote.response.ResVideo
import com.example.domain.entity.SearchingViewType
import com.example.domain.entity.Video

fun ResVideo.toVideo() = Video(
    meta = this.meta.toMeta(),
    documents = this.documents.toDocumentList(
        searchingViewType = SearchingViewType.Video,
    )
)
