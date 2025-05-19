package com.travelassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event> : ViewModel() {
    protected abstract val _state: MutableStateFlow<State>
    val state: StateFlow<State> = _state.asStateFlow()

    abstract fun onEvent(event: Event)

    protected fun updateState(update: (State) -> State) {
        viewModelScope.launch {
            _state.update(update)
        }
    }

    protected fun <T> withState(block: (State) -> T): T {
        return block(_state.value)
    }
}

sealed class UiState<out T> {
    data object Initial : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}

abstract class BaseUiViewModel<State, Event> : BaseViewModel<UiState<State>, Event>() {
    protected fun setLoading() {
        updateState { UiState.Loading }
    }

    protected fun setError(message: String) {
        updateState { UiState.Error(message) }
    }

    protected fun setSuccess(data: State) {
        updateState { UiState.Success(data) }
    }

    protected fun <T> handleResult(
        block: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = { setError(it.message ?: "An error occurred") }
    ) {
        viewModelScope.launch {
            try {
                setLoading()
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
} 