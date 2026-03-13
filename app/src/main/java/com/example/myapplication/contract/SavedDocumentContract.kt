package com.example.myapplication.contract

import androidx.annotation.StringRes
import com.example.domain.entity.Document
import com.example.myapplication.common.ViewEffect
import com.example.myapplication.common.ViewEvent
import com.example.myapplication.common.ViewState

class SavedDocumentContract {

    sealed class Event : ViewEvent {
        data object OnLoadSavedDocuments : Event()
        data class OnClickSavedDocument(val position: Int) : Event()
        data class OnRemoveSavedDocument(val position: Int) : Event()
        data class OnShareSavedDocument(val position: Int) : Event()
        data class OnCopySavedDocumentLink(val position: Int) : Event()
    }

    data class UiState(
        val savedDocuments: List<Document> = emptyList(),
    ) : ViewState

    sealed class Effect : ViewEffect {
        data class NavigateToDetail(val uri: String) : Effect()
        data class ShareLink(val link: String) : Effect()
        data class CopyLink(val link: String) : Effect()
        data class ShowMessage(
            @StringRes val messageResId: Int,
        ) : Effect()
    }
}
