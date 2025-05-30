package com.travelassistant.ui.viewmodel.register

import androidx.core.util.PatternsCompat // For email validation
import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.repository.AuthRepository
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.notifications.NotificationsState
import com.travelassistant.util.Result // Your custom Result wrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Define constants for password policy
private const val MIN_PASSWORD_LENGTH = 8

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository // Injects the authentication repository
) : BaseUiViewModel<RegisterDataState, RegisterEvent>() { // Extends BaseUiViewModel
    // Holds the current UI state, initialized with an initial success state holding default RegisterDataState.
    // BaseUiViewModel expects UiState<State>, so UiState.Success with initial data is appropriate.

    public override val _state = MutableStateFlow<UiState<RegisterDataState>>(UiState.Initial)



    /**
     * Handles events dispatched from the UI (RegisterScreen).
     * @param event The event to handle.
     */
    override fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NameChanged -> {
                // Update the name in the current data state
                updateDataState { it.copy(name = event.name) }
                validateInputs()
            }
            is RegisterEvent.EmailChanged -> {
                // Update the email in the current data state
                updateDataState { it.copy(email = event.email) }
                validateInputs()
            }
            is RegisterEvent.PasswordChanged -> {
                // Update the password in the current data state
                updateDataState { it.copy(password = event.password) }
                validateInputs()
            }
            RegisterEvent.SignUpClicked -> {
                // Initiate the registration attempt
                attemptRegistration()
            }
        }
    }

    /**
     * Validates the current input fields (name, email, password)
     * and updates the isSignUpButtonEnabled flag in the RegisterDataState.
     */
    private fun validateInputs() {
        // Access the current RegisterDataState, assuming it's within UiState.Success
        val currentData = (_state.value as? UiState.Success)?.data ?: return

        // Basic email validation: checks for common email pattern.
        val isEmailValid = PatternsCompat.EMAIL_ADDRESS.matcher(currentData.email).matches()

        // Basic password policy: checks for minimum length.
        val isPasswordValid = currentData.password.length >= MIN_PASSWORD_LENGTH

        // Determine if the sign-up button should be enabled.
        val isFormValid = currentData.name.isNotBlank() &&
                isEmailValid &&
                isPasswordValid

        // Update the data state with the new button enabled status.
        updateDataState { it.copy(isSignUpButtonEnabled = isFormValid) }
    }

    /**
     * Attempts to register the user using the data from the current state.
     * Handles UI updates for loading, success, and error states.
     */
    private fun attemptRegistration() {
        // Access the current RegisterDataState
        val currentData = (_state.value as? UiState.Success)?.data ?: return

        // Do not proceed if the form is not valid (button shouldn't be clickable, but as a safeguard)
        if (!currentData.isSignUpButtonEnabled) return

        // Create the request DTO for the registration API call
        val userRegisterRequest = UserRegisterRequest(
            name = currentData.name.trim(),
            email = currentData.email.trim(),
            password = currentData.password // Passwords are typically not trimmed
        )

        // Use the handleResult helper from BaseUiViewModel for asynchronous operation
        handleResult(
            block = {
                authRepository.registerUser(userRegisterRequest)
            },
            onSuccess = { result -> // result is of type com.travelassistant.util.Result<UserResponse>
                when (result) {
                    is Result.Success -> {
                        updateDataState {
                            it.copy(
                                registrationMessage = "Registration successful! Please log in."
                            )
                        }
                        // Get the current data to pass to setSuccess
                        val currentData = (_state.value as? UiState.Success)?.data ?: RegisterDataState()
                        setSuccess(currentData.copy(registrationMessage = "Registration successful! Please log in."))
                    }
                    is Result.Error -> {
                        // Use result.message from your Result.Error class
                        setError(result.message ?: "Registration failed.") // Use result.message
                        updateDataState { it.copy(registrationMessage = null) }
                    }
                    Result.Loading -> {
                        // The handleResult in BaseUiViewModel already sets a loading state.
                        // You might not need to do anything specific here if your
                        // authRepository.registerUser emits Result.Loading before completing.
                        // If it does, this branch will be hit.
                        // For now, we can let the outer setLoading from handleResult manage it.
                        // If you wanted to update the inner data state specifically while loading:
                        // updateDataState { it.copy(registrationMessage = "Processing...") }
                        // But typically, the global UiState.Loading is sufficient.
                    }
                    // No 'else' needed if all sealed class subtypes are covered.
                }
            },
            onError = { exception ->
                setError(exception.message ?: "An unexpected error occurred during registration.")
                updateDataState { it.copy(registrationMessage = null) }
            }
        )
    }

    /**
     * Helper function to update the RegisterDataState within the current UiState.
     * This assumes the current state is UiState.Success containing RegisterDataState.
     * If it's Loading or Error, this won't update the data part but will be wrapped by setSuccess.
     */
    private fun updateDataState(update: (RegisterDataState) -> RegisterDataState) {
        val currentUiState = _state.value
        if (currentUiState is UiState.Success) {
            // We are already in a success state, just update its data
            _state.update {
                UiState.Success(update(currentUiState.data))
            }
        } else {
            // If not in Success (e.g. Initial, Loading, Error), and we need to update data fields,
            // we'd typically transition to Success with the new data.
            // However, text field updates should ideally happen on an existing Success<RegisterDataState>.
            // For simplicity, let's assume for field changes, we're always building upon a Success state
            // that holds the RegisterDataState.
            // If _state is Initial, the first event should transition it.
            // The BaseUiViewModel's updateState { UiState.Success(newData) } handles this.
            // Let's make it simpler: just update the content of RegisterDataState and let setSuccess wrap it.
            val currentData = (currentUiState as? UiState.Success)?.data ?: RegisterDataState()
            setSuccess(update(currentData))
        }
    }

    // Initialize the state to Success with default data so that onEvent updates can modify it.
    init {
     //   _state.value = UiState.Success(RegisterDataState())
    }
}