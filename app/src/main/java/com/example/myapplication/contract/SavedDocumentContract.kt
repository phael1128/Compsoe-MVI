package com.example.myapplication.contract

import com.example.domain.entity.Document
import com.example.myapplication.common.ViewEffect
import com.example.myapplication.common.ViewEvent
import com.example.myapplication.common.ViewState

class SavedDocumentContract {

    sealed class Event : ViewEvent {
        data object OnLoadSavedDocuments : Event()
    }

    data class UiState(
        val savedDocuments: List<Document> = emptyList(),
    ) : ViewState

    sealed class Effect : ViewEffect
}
