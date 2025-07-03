package com.example.myapplication.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DocumentEntity
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
    ) : BaseViewModel() {
        // 유저가 검색한 키워드
        val userSearchingData: MutableState<String> = mutableStateOf("")

        // 유저가 검색한 결과
        val searchingUiState: MutableState<List<DocumentEntity>> = mutableStateOf(emptyList())

        override fun handleIntent(intent: Intent) {
            when (intent) {
                is SearchingIntent.Searching -> {
                    getSearchingResult()
                }
                is SearchingIntent.SearchingMoreData -> {
                    searchData()
                }
            }
        }

        private fun getSearchingResult() {
            viewModelScope.launch(coroutineExceptionHandler) {
                if (mediaSearchResultUseCase.getLastKeyword() == userSearchingData.value) {
                    return@launch
                }

                initializeSearchingUiState()
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
        }

        private fun initializeSearchingUiState() {
            searchingUiState.value = emptyList()
        }
    }
