package com.travelassistant.ui.viewmodel.register

/**
 * Represents the data fields and UI-specific state for the Registration screen.
 * This state is wrapped by UiState (e.g., UiState.Success(RegisterDataState(...))) in the ViewModel.
 */
data class RegisterDataState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isSignUpButtonEnabled: Boolean = false,
    val registrationMessage: String? = null // For success or specific error messages not part of the general UiState.Error
)