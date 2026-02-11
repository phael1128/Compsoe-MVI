package com.example.myapplication.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SavedDocumentResultUseCase
import com.example.myapplication.contract.SavedDocumentContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedDocumentViewModel
    @Inject
    constructor(
        private val savedDocumentResultUseCase: SavedDocumentResultUseCase,
    ) : BaseViewModel<SavedDocumentContract.UiState, SavedDocumentContract.Event, SavedDocumentContract.Effect>() {

        override fun createInitialState(): SavedDocumentContract.UiState = SavedDocumentContract.UiState()

        override fun handleEvents(event: SavedDocumentContract.Event) {
            when (event) {
                is SavedDocumentContract.Event.OnLoadSavedDocuments -> {
                    getSavedDocumentList()
                }
            }
        }

        private fun getSavedDocumentList() {
            viewModelScope.launch(coroutineExceptionHandler) {
                val documents = savedDocumentResultUseCase.getSavedDocumentEntity()
                setState { copy(savedDocuments = documents) }
            }
        }
    }
