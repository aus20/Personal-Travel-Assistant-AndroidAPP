package com.travelassistant.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event> : ViewModel() {
    // This abstract property MUST be overridden and initialized by concrete subclasses.
    protected abstract val _state: MutableStateFlow<State>

    // Corrected: Uses a getter to ensure _state from the subclass is initialized when accessed.
    val state: StateFlow<State> get() = _state.asStateFlow()

    abstract fun onEvent(event: Event)

    /**
     * Updates the underlying MutableStateFlow (_state).
     * Assumes _state has been initialized by the concrete subclass.
     */
    protected fun updateState(update: (State) -> State) {
        viewModelScope.launch {
            _state.update(update) // Direct access, assumes subclass initialized _state
        }
    }

    /**
     * Allows synchronous access to the current value of _state.
     * Assumes _state has been initialized by the concrete subclass.
     */
    protected fun <T> withState(block: (State) -> T): T {
        return block(_state.value) // Direct access, assumes subclass initialized _state
    }
}

/**
 * A sealed class representing the different states of a UI component or screen that involves asynchronous data loading.
 * It can be Initial, Loading, Error with a message, or Success with data of type T.
 */
sealed class UiState<out T> {
    data object Initial : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
}

/**
 * An abstract ViewModel that extends BaseViewModel and is specialized for UIs
 * that follow the common pattern of Loading/Success/Error states, represented by UiState<State>.
 * 'Data' here is the actual data type to be held within UiState.Success.
 *
 * Concrete ViewModels extending this (e.g., RegisterViewModel) MUST override and initialize _state.
 * Example in a concrete ViewModel:
 * override val _state = MutableStateFlow<UiState<MySpecificData>>(UiState.Initial) // or UiState.Success(MySpecificData())
 */
abstract class BaseUiViewModel<Data, Event> : BaseViewModel<UiState<Data>, Event>() {

    // Note: _state is NOT initialized here. It remains abstract from BaseViewModel
    // and MUST be overridden in the concrete ViewModel that extends BaseUiViewModel.
    // For example, RegisterViewModel should have:
    // override val _state = MutableStateFlow<UiState<RegisterDataState>>(UiState.Success(RegisterDataState()))

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