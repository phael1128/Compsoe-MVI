package com.example.myapplication.contract

import com.example.domain.entity.Document
import com.example.myapplication.common.ViewEffect
import com.example.myapplication.common.ViewEvent
import com.example.myapplication.common.ViewState

class SearchingContract {

    sealed class Event : ViewEvent {
        data class OnSearchQueryChanged(val query: String) : Event()
        data object OnSearchClick : Event()
        data object OnLoadMoreData : Event()
        data class OnSaveDocument(val position: Int) : Event()
    }

    data class UiState(
        val searchQuery: String = "",
        val documents: List<Document> = emptyList(),
        val isLoading: Boolean = false,
    ) : ViewState

    sealed class Effect : ViewEffect {
        data class NavigateToDetail(val uri: String) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}
