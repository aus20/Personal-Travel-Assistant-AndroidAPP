package com.travelassistant.ui.viewmodel.login

/**
 * Represents the data fields and UI-specific state for the Login screen.
 * This state is wrapped by UiState in the LoginViewModel.
 */
data class LoginDataState(
    val email: String = "",
    val password: String = "",
    val isLoginButtonEnabled: Boolean = false,
    val loginMessage: String? = null // For success/error messages not part of general UiState.Error
)