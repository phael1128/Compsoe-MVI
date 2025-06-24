package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.common.Intent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("phael", "exception -> ${throwable.message}")
    }

    private val _intent: MutableSharedFlow<Intent> = MutableSharedFlow()

    init {
        subscribeIntent()
    }

    fun setIntent(intent: Intent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun subscribeIntent() {
        viewModelScope.launch {
            _intent.collect {
                handleIntent(it)
            }
        }
    }

    protected open fun handleIntent(intent: Intent) {}
}