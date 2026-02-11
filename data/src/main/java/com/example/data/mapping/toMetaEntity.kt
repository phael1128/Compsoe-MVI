package com.example.data.mapping

import com.example.data.datasource.remote.response.ResMeta
import com.example.domain.entity.Meta

fun ResMeta.toMeta() = Meta(
    total_count = total_count,
    pageable_count = pageable_count,
    is_end = is_end
)
