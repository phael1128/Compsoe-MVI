package com.example.myapplication.model

import com.example.myapplication.common.Intent

open class SearchingIntent : Intent {
    object Searching : SearchingIntent()

    object SearchingMoreData : SearchingIntent()

    data class SaveDocument(val position: Int) : SearchingIntent()
}
