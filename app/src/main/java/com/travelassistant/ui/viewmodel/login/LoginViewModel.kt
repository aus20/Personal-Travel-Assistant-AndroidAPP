package com.travelassistant.ui.viewmodel.login

import android.util.Log // For logging
import androidx.core.util.PatternsCompat
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging // Import FirebaseMessaging
import com.travelassistant.data.remote.dto.request.FcmTokenRequest // Import FcmTokenRequest
import com.travelassistant.data.remote.dto.request.UserLoginRequest
import com.travelassistant.data.remote.dto.response.JwtLoginResponse
import com.travelassistant.data.repository.AuthRepository
import com.travelassistant.data.session.SessionManager
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.util.Result // Your custom Result wrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch // Ensure this is imported
import kotlinx.coroutines.tasks.await // Import await()
import javax.inject.Inject

private const val LOGIN_VIEW_MODEL_TAG = "LoginViewModel" // Tag for logging

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val sessionManager: SessionManager
) : BaseUiViewModel<LoginDataState, LoginEvent>() {

    override val _state = MutableStateFlow<UiState<LoginDataState>>(UiState.Success(LoginDataState()))

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                updateDataState { it.copy(email = event.email) }
                validateInputs()
            }
            is LoginEvent.PasswordChanged -> {
                updateDataState { it.copy(password = event.password) }
                validateInputs()
            }
            LoginEvent.LoginClicked -> {
                attemptLogin()
            }
        }
    }

    private fun validateInputs() {
        val currentData = (_state.value as? UiState.Success)?.data ?: return
        val isEmailValid = PatternsCompat.EMAIL_ADDRESS.matcher(currentData.email).matches()
        val isPasswordValid = currentData.password.isNotBlank()
        val isFormValid = isEmailValid && isPasswordValid
        updateDataState { it.copy(isLoginButtonEnabled = isFormValid) }
    }

    private fun attemptLogin() {
        val currentData = (_state.value as? UiState.Success)?.data ?: return
        if (!currentData.isLoginButtonEnabled) return

        val userLoginRequest = UserLoginRequest(
            email = currentData.email.trim(),
            password = currentData.password
        )
        Log.d(LOGIN_VIEW_MODEL_TAG, "Attempting login for: ${userLoginRequest.email}")

        handleResult(
            block = {
                authRepository.loginUser(userLoginRequest)
            },
            onSuccess = { loginResult: Result<JwtLoginResponse> ->
                when (loginResult) {
                    is Result.Success -> {
                        Log.i(LOGIN_VIEW_MODEL_TAG, "Login API call successful.")
                        sessionManager.saveAuthToken(loginResult.data.token)
                        loginResult.data.user?.let { userDetails ->
                            sessionManager.saveUserDetails(userDetails)
                        }
                        Log.i(LOGIN_VIEW_MODEL_TAG, "Auth token and user details saved.")

                        // After successful login and session saving, send FCM token
                        sendFcmTokenToServer() // Call the new function

                        // It's important that setSuccess for login is called AFTER FCM token logic
                        // or the UI might navigate away before FCM token is sent.
                        // Or, the FCM token sending can be a separate indication to the UI if needed.
                        // For now, we'll make it part of the login success sequence.
                        // The setSuccess will trigger navigation via LaunchedEffect in the LoginScreen.
                        updateDataState {
                            it.copy(loginMessage = "Login successful!")
                        }
                        setSuccess(currentData.copy(loginMessage = "Login successful!"))
                    }
                    is Result.Error -> {
                        Log.e(LOGIN_VIEW_MODEL_TAG, "Login API call failed: ${loginResult.message}")
                        setError(loginResult.message ?: "Login failed. Please check your credentials.")
                        updateDataState { it.copy(loginMessage = null) }
                    }
                    Result.Loading -> {
                        Log.d(LOGIN_VIEW_MODEL_TAG, "Login API call is loading (repository emitted).")
                        // Already handled by handleResult's setLoading()
                    }
                }
            },
            onError = { exception ->
                Log.e(LOGIN_VIEW_MODEL_TAG, "Exception during login attempt: ${exception.message}", exception)
                setError(exception.message ?: "An unexpected error occurred during login.")
                updateDataState { it.copy(loginMessage = null) }
            }
        )
    }

    /**
     * Retrieves the current FCM token from Firebase and sends it to the backend server.
     * This should be called after a user successfully logs in and an auth token is available.
     */
    private fun sendFcmTokenToServer() {
        viewModelScope.launch {
            val authToken = sessionManager.getAuthToken() // Retrieve the saved JWT token

            if (authToken.isNullOrBlank()) {
                Log.e(LOGIN_VIEW_MODEL_TAG, "Auth Token not found for sending FCM token. Login might have failed or token not saved.")
                // Optionally, update UI state to reflect this specific error if critical
                // For now, just logging, as login success path should ensure token is present.
                return@launch
            }

            var currentFcmToken: String? = null
            try {
                Log.d(LOGIN_VIEW_MODEL_TAG, "Attempting to retrieve FCM token from Firebase.")
                currentFcmToken = FirebaseMessaging.getInstance().token.await() // Asynchronously get the token
                Log.i(LOGIN_VIEW_MODEL_TAG, "FCM Token retrieved from Firebase: $currentFcmToken")
            } catch (e: Exception) {
                Log.e(LOGIN_VIEW_MODEL_TAG, "Failed to retrieve FCM Token from Firebase.", e)
                // Decide if this failure should block the user or be a silent failure.
                // For now, we log and proceed, but you might want to inform the user
                // or retry later if FCM is critical at this point.
                // updateDataState { it.copy(loginMessage = "Login successful, but couldn't update notification preferences.") }
                // setError("Couldn't update notification preferences. Please try again later.")
                return@launch // Stop if FCM token cannot be retrieved
            }

            if (currentFcmToken.isNullOrBlank()) {
                Log.e(LOGIN_VIEW_MODEL_TAG, "Retrieved FCM Token is null or blank. Cannot send to server.")
                return@launch
            }

            Log.d(LOGIN_VIEW_MODEL_TAG, "Sending FCM Token to server. Auth: Bearer $authToken, FCM: $currentFcmToken")
            val request = FcmTokenRequest(token = currentFcmToken)

            // We are not using handleResult here as the primary login state is already managed.
            // This is a secondary operation. We'll log its success/failure.
            // If this call failing needs to show a prominent error, you might need another UI state field.
            try {
                // Add "Bearer " prefix to the token for the Authorization header
                val formattedAuthToken = if (authToken.startsWith("Bearer ")) authToken else "Bearer $authToken"
                val fcmUpdateResult = authRepository.updateFcmToken(formattedAuthToken, request)
                // Assuming authRepository.updateFcmToken also returns your Result wrapper
                when (fcmUpdateResult) {
                    is Result.Success -> {
                        Log.i(LOGIN_VIEW_MODEL_TAG, "FCM Token successfully updated on the server.")
                        // You might want to store that the FCM token has been synced
                        // sessionManager.saveFcmTokenSynced(true) // Example: new method in SessionManager
                    }
                    is Result.Error -> {
                        Log.e(LOGIN_VIEW_MODEL_TAG, "Failed to update FCM Token on server: ${fcmUpdateResult.message}")
                        // sessionManager.saveFcmTokenSynced(false)
                        // Optionally, inform the user non-critically, or schedule a retry
                    }
                    Result.Loading -> {
                        Log.d(LOGIN_VIEW_MODEL_TAG, "FCM Token update is loading.")
                    }
                }
            } catch (e: Exception) {
                Log.e(LOGIN_VIEW_MODEL_TAG, "Exception while sending FCM token to server: ${e.message}", e)
                // sessionManager.saveFcmTokenSynced(false)
                // Handle error, maybe retry later with WorkManager if this is critical
            }
        }
    }

    private fun updateDataState(update: (LoginDataState) -> LoginDataState) {
        val currentUiState = _state.value
        if (currentUiState is UiState.Success) {
            _state.update { UiState.Success(update(currentUiState.data)) }
        } else {
            val currentData = (currentUiState as? UiState.Success)?.data ?: LoginDataState()
            setSuccess(update(currentData))
        }
    }
}