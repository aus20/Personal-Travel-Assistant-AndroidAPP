package com.travelassistant.ui.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelassistant.data.session.SessionManager
import com.travelassistant.ui.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. A simple data class to hold only the data needed for this screen.
data class ProfileDisplayState(
    val name: String = "",
    val email: String = ""
)

// 1. Define a sealed class for one-time navigation events.
sealed class ProfileNavigationEvent {
    data object NavigateToLogin : ProfileNavigationEvent()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // 2. Inject SessionManager instead of PreferencesRepository.
    internal val sessionManager: SessionManager
) : ViewModel() { // We can extend ViewModel directly as BaseUiViewModel is for more complex event handling

    // 3. The UI state flow, wrapping our new simple state class.
    private val _uiState = MutableStateFlow<UiState<ProfileDisplayState>>(UiState.Initial)
    val uiState: StateFlow<UiState<ProfileDisplayState>> = _uiState.asStateFlow()

    // 2. Create a SharedFlow to emit navigation events.
    private val _navigationEvent = MutableSharedFlow<ProfileNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // 4. Load the profile information when the ViewModel is created.
    init {
        loadProfile()
    }

    // 5. The core logic to fetch user data from the session.
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val userDetails = sessionManager.getUserDetails() // Fetch from session

            if (userDetails != null) {
                // On success, update the state with user's name and email
                _uiState.value = UiState.Success(
                    ProfileDisplayState(
                        name = userDetails.name,
                        email = userDetails.email
                    )
                )
            } else {
                // If no user is logged in or data is missing, post an error state.
                _uiState.value = UiState.Error("User not logged in or profile data is unavailable.")
            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _navigationEvent.emit(ProfileNavigationEvent.NavigateToLogin)
        }
    }
}