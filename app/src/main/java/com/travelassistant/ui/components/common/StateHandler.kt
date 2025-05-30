package com.travelassistant.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun <T> StateHandler(
    state: T,
    isLoading: Boolean,
    error: String?,
    onErrorShown: () -> Unit,
    content: @Composable (T) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        content(state)

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (error != null) {
            LaunchedEffect(error) {
                snackbarHostState.showSnackbar(error)
                onErrorShown()
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun <T> StateHandler(
    state: T,
    isLoading: Boolean,
    error: String?,
    onErrorShown: () -> Unit,
    emptyState: @Composable () -> Unit,
    content: @Composable (T) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state is List<*> && state.isEmpty() -> {
                emptyState()
            }
            else -> {
                content(state)
            }
        }

        if (error != null) {
            LaunchedEffect(error) {
                snackbarHostState.showSnackbar(error)
                onErrorShown()
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun <T> ObserveState(
    stateFlow: Flow<T>,
    onStateChange: (T) -> Unit
) {
    LaunchedEffect(stateFlow) {
        stateFlow.collectLatest { state ->
            onStateChange(state)
        }
    }
} 