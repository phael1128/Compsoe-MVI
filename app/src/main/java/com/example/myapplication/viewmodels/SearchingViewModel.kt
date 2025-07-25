package com.example.myapplication.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Document
import com.example.domain.usecase.SavedDocumentResultUseCase
import com.example.domain.usecase.MediaSearchResultUseCase
import com.example.myapplication.common.Intent
import com.example.myapplication.model.SearchingIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel
    @Inject
    constructor(
        private val mediaSearchResultUseCase: MediaSearchResultUseCase,
        private val savedDocumentResultUseCase: SavedDocumentResultUseCase
    ) : BaseViewModel() {
        // 유저가 검색한 키워드
        val userSearchingData: MutableState<String> = mutableStateOf("")

        // 유저가 검색한 결과
        val searchingUiState: MutableState<List<Document>> = mutableStateOf(emptyList())

        val isLoadingState: MutableState<Boolean> = mutableStateOf(false)

        override fun handleIntent(intent: Intent) {
            when (intent) {
                is SearchingIntent.Searching -> {
                    getSearchingResult()
                }
                is SearchingIntent.SearchingMoreData -> {
                    searchData()
                }
                is SearchingIntent.SaveDocument -> {
                    saveDocument(intent.position)
                }
            }
        }

        private fun getSearchingResult() {
            viewModelScope.launch(coroutineExceptionHandler) {
                if (mediaSearchResultUseCase.getLastKeyword() == userSearchingData.value) {
                    return@launch
                }

                initializeSearchingUiState()
                isLoadingState.value = true
                searchMoreData()
            }
        }

        private fun searchData() {
            viewModelScope.launch(coroutineExceptionHandler) {
                searchMoreData()
            }
        }

        private suspend fun searchMoreData() {
            searchingUiState.value +=
                mediaSearchResultUseCase(
                    query = userSearchingData.value,
                )
            isLoadingState.value = false
        }

        private fun initializeSearchingUiState() {
            searchingUiState.value = emptyList()
        }

        private fun saveDocument(position: Int) {
            viewModelScope.launch(coroutineExceptionHandler) {
                val saveDocument = searchingUiState.value[position]
                savedDocumentResultUseCase.insertDocumentEntity(saveDocument)
            }
        }
    }
