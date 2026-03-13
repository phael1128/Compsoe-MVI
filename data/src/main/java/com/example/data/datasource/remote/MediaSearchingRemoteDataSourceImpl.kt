package com.example.data.datasource.remote

import com.example.data.datasource.remote.network.HttpUrlConnectionClient
import com.example.data.datasource.remote.network.RemoteResponse
import com.example.data.datasource.remote.response.ResImage
import com.example.data.datasource.remote.response.ResVideo
import javax.inject.Inject

class MediaSearchingRemoteDataSourceImpl
    @Inject
    constructor(
        private val httpUrlConnectionClient: HttpUrlConnectionClient,
    ) : MediaSearchingRemoteDataSource {
        override suspend fun searchImageResult(
            query: String,
            page: Int,
            size: Int,
        ): RemoteResponse<ResImage> =
            httpUrlConnectionClient.get(
                path = IMAGE_SEARCH_PATH,
                queryParams =
                    mapOf(
                        "query" to query,
                        "page" to page.toString(),
                        "size" to size.toString(),
                    ),
                responseClass = ResImage::class.java,
            )

        override suspend fun searchVideoResult(
            query: String,
            page: Int,
            size: Int,
        ): RemoteResponse<ResVideo> =
            httpUrlConnectionClient.get(
                path = VIDEO_SEARCH_PATH,
                queryParams =
                    mapOf(
                        "query" to query,
                        "page" to page.toString(),
                        "size" to size.toString(),
                    ),
                responseClass = ResVideo::class.java,
            )

        companion object {
            private const val IMAGE_SEARCH_PATH = "v2/search/image"
            private const val VIDEO_SEARCH_PATH = "v2/search/vclip"
        }
    }
