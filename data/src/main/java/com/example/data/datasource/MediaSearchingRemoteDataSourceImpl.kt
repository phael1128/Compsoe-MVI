package com.example.data.datasource

import com.example.data.datasource.remote.MediaSearchingRemoteDataSource
import com.example.data.datasource.remote.network.NetworkAPI
import com.example.data.datasource.remote.response.ResImage
import com.example.data.datasource.remote.response.ResVideo
import javax.inject.Inject

class MediaSearchingRemoteDataSourceImpl @Inject constructor(
    private val networkAPI: NetworkAPI
): MediaSearchingRemoteDataSource {
    override suspend fun searchImageResult(query: String, page: Int, size: Int): ResImage {
        return networkAPI.searchImageResult(
            query = query,
            page = page,
            size = size
        )
    }

    override suspend fun searchVideoResult(query: String, page: Int, size: Int): ResVideo {
        return networkAPI.searchVideoResult(
            query = query,
            page = page,
            size = size
        )
    }

}