package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.ViewEffect
import com.example.myapplication.common.ViewEvent
import com.example.myapplication.common.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState : ViewState, Event : ViewEvent, Effect : ViewEffect> :
    ViewModel() {

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("phael", "exception -> ${throwable.message}")
    }

    abstract fun createInitialState(): UiState

    abstract fun handleEvents(event: Event)

    private val initialState: UiState by lazy { createInitialState() }

    private val _uiState: MutableStateFlow<UiState> by lazy { MutableStateFlow(initialState) }
    val uiState: StateFlow<UiState> by lazy { _uiState.asStateFlow() }

    val currentState: UiState get() = _uiState.value

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeEvents()
    }

    fun setEvent(event: Event) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    protected fun setState(reduce: UiState.() -> UiState) {
        _uiState.value = currentState.reduce()
    }

    protected fun setEffect(builder: () -> Effect) {
        viewModelScope.launch {
            _effect.send(builder())
        }
    }

    private fun subscribeEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }
}
