package com.example.myapplication.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Document
import com.example.domain.entity.SearchingViewType
import com.example.domain.usecase.SavedDocumentResultUseCase
import com.example.myapplication.R
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
                is SavedDocumentContract.Event.OnClickSavedDocument -> {
                    openSavedDocument(event.position)
                }
                is SavedDocumentContract.Event.OnRemoveSavedDocument -> {
                    removeSavedDocument(event.position)
                }
                is SavedDocumentContract.Event.OnShareSavedDocument -> {
                    shareSavedDocument(event.position)
                }
                is SavedDocumentContract.Event.OnCopySavedDocumentLink -> {
                    copySavedDocumentLink(event.position)
                }
            }
        }

        private fun getSavedDocumentList() {
            viewModelScope.launch(coroutineExceptionHandler) {
                val documents = savedDocumentResultUseCase.getSavedDocumentEntity()
                setState { copy(savedDocuments = documents) }
            }
        }

        private fun openSavedDocument(position: Int) {
            val document = currentState.savedDocuments.getOrNull(position) ?: return
            val link = document.detailLink() ?: return

            setEffect {
                SavedDocumentContract.Effect.NavigateToDetail(link)
            }
        }

        private fun removeSavedDocument(position: Int) {
            viewModelScope.launch(coroutineExceptionHandler) {
                val document = currentState.savedDocuments.getOrNull(position) ?: return@launch
                savedDocumentResultUseCase.deleteDocumentEntity(document)
                val documents = savedDocumentResultUseCase.getSavedDocumentEntity()

                setState { copy(savedDocuments = documents) }
                setEffect {
                    SavedDocumentContract.Effect.ShowMessage(R.string.saved_document_removed_message)
                }
            }
        }

        private fun shareSavedDocument(position: Int) {
            val document = currentState.savedDocuments.getOrNull(position) ?: return
            val link = document.detailLink() ?: return

            setEffect {
                SavedDocumentContract.Effect.ShareLink(link)
            }
        }

        private fun copySavedDocumentLink(position: Int) {
            val document = currentState.savedDocuments.getOrNull(position) ?: return
            val link = document.detailLink() ?: return

            setEffect {
                SavedDocumentContract.Effect.CopyLink(link)
            }
        }

        private fun Document.detailLink(): String? =
            when (searchingViewType) {
                SearchingViewType.Image -> docUrl
                SearchingViewType.Video -> url
                null -> docUrl ?: url
            }?.takeIf { it.isNotBlank() }
    }
