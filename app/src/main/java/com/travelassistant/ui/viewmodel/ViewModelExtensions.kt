package com.travelassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, E> : ViewModel() { // Renamed type parameters for clarity
    // This abstract property MUST be overridden and initialized by concrete subclasses.
    protected abstract val _state: MutableStateFlow<S>

    // Corrected: Uses a getter to ensure _state from the subclass is initialized when accessed.
    val state: StateFlow<S> get() = _state.asStateFlow()

    abstract fun onEvent(event: E)

    protected fun updateState(update: (S) -> S) {
        viewModelScope.launch {
            _state.update(update)
        }
    }

    protected fun <T> withState(block: (S) -> T): T {
        return block(_state.value)
    }
}

sealed class UiState<out T> {
    data object Initial : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}

abstract class BaseUiViewModel<Data, Event> : BaseViewModel<UiState<Data>, Event>() {

    // BaseUiViewModel now provides the concrete implementation and initialization for _state.
    // Any ViewModel extending BaseUiViewModel (like LoginViewModel, RegisterViewModel)
    // will inherit this _state by default. They can still override it if they need a
    // different initial value (e.g., UiState.Success(SpecificInitialData())).
    override val _state = MutableStateFlow<UiState<Data>>(UiState.Initial)

    protected fun setLoading() {
        updateState { UiState.Loading }
    }

    protected fun setError(message: String) {
        updateState { UiState.Error(message) }
    }

    protected fun setSuccess(data: Data) {
        updateState { UiState.Success(data) }
    }

    protected fun <T> handleResult(
        block: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit = { setError(it.message ?: "An error occurred") }
    ) {
        viewModelScope.launch {
            setLoading()
            try {
                val result = block()
                onSuccess(result)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}