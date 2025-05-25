package com.travelassistant.ui.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
/*
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val networkStateManager: NetworkStateManager
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        viewModelScope.launch {
            networkStateManager.isOnline.collect { isOnline ->
                _isOnline.value = isOnline
            }
        }
        //loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            
            try {
                val userProfile = preferencesRepository.getUserProfile()
                _profileState.value = ProfileState.Success(userProfile = userProfile)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun updateProfileName(name: String) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedProfile = currentState.userProfile.copy(
                    name = name,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update name: ${e.message}")
            }
        }
    }

    fun updatePhoneNumber(phoneNumber: String?) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedProfile = currentState.userProfile.copy(
                    phoneNumber = phoneNumber,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update phone number: ${e.message}")
            }
        }
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    notificationSettings = settings
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update notification settings: ${e.message}")
            }
        }
    }

    fun updateSearchPreferences(preferences: SearchPreferences) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    searchPreferences = preferences
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update search preferences: ${e.message}")
            }
        }
    }

    fun updateDisplayPreferences(preferences: DisplayPreferences) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    displayPreferences = preferences
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update display preferences: ${e.message}")
            }
        }
    }

    fun updateSyncPreferences(preferences: SyncPreferences) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    syncPreferences = preferences
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update sync preferences: ${e.message}")
            }
        }
    }

    private fun updateErrorState(errorMessage: String) {
        _profileState.update { currentState ->
            when (currentState) {
                is ProfileState.Success -> currentState.copy(
                    error = errorMessage,
                    isLoading = false
                )
                else -> ProfileState.Error(errorMessage)
            }
        }
    }
} */