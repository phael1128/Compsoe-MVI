package com.example.myapplication.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.MediaSearchResultUseCase
import com.example.domain.usecase.SavedDocumentResultUseCase
import com.example.myapplication.contract.SearchingContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel
    @Inject
    constructor(
        private val mediaSearchResultUseCase: MediaSearchResultUseCase,
        private val savedDocumentResultUseCase: SavedDocumentResultUseCase,
    ) : BaseViewModel<SearchingContract.UiState, SearchingContract.Event, SearchingContract.Effect>() {

        override fun createInitialState(): SearchingContract.UiState = SearchingContract.UiState()

        override fun handleEvents(event: SearchingContract.Event) {
            when (event) {
                is SearchingContract.Event.OnSearchQueryChanged -> {
                    setState { copy(searchQuery = event.query) }
                }
                is SearchingContract.Event.OnSearchClick -> {
                    getSearchingResult()
                }
                is SearchingContract.Event.OnLoadMoreData -> {
                    searchData()
                }
                is SearchingContract.Event.OnSaveDocument -> {
                    saveDocument(event.position)
                }
            }
        }

        private fun getSearchingResult() {
            viewModelScope.launch(coroutineExceptionHandler) {
                if (mediaSearchResultUseCase.getLastKeyword() == currentState.searchQuery) {
                    return@launch
                }

                setState { copy(documents = emptyList(), isLoading = true) }
                searchMoreData()
            }
        }

        private fun searchData() {
            viewModelScope.launch(coroutineExceptionHandler) {
                searchMoreData()
            }
        }

        private suspend fun searchMoreData() {
            val newDocuments = mediaSearchResultUseCase(query = currentState.searchQuery)
            setState { copy(documents = documents + newDocuments, isLoading = false) }
        }

        private fun saveDocument(position: Int) {
            viewModelScope.launch(coroutineExceptionHandler) {
                val saveDocument = currentState.documents[position]
                savedDocumentResultUseCase.insertDocumentEntity(saveDocument)
            }
        }
    }
