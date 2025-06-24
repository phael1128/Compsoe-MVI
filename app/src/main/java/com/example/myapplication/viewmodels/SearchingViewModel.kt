package com.example.myapplication.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.DocumentEntity
import com.example.domain.usecase.MediaSearchResultUseCase
import com.example.myapplication.common.Intent
import com.example.myapplication.model.SearchingIntent
import com.example.myapplication.model.SearchingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val mediaSearchResultUseCase: MediaSearchResultUseCase
): BaseViewModel() {

    val userSearchingData: MutableState<String> = mutableStateOf("")

    val searchingUiState: MutableState<List<DocumentEntity>> = mutableStateOf<List<DocumentEntity>>(emptyList())

    override fun handleIntent(intent: Intent) {
        when(intent) {
            is SearchingIntent.Searching -> {
                getSearchingResult()
            }
        }
    }

    private fun getSearchingResult() {
        viewModelScope.launch(coroutineExceptionHandler) {
            searchingUiState.value = mediaSearchResultUseCase(
                query = userSearchingData.value,
                page = 1,
                size = 30
            )
        }
    }
}