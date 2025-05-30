package com.travelassistant.ui.viewmodel.login

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    data object LoginClicked : LoginEvent()
    // Could add an event like LoginMessageShown if needed to clear one-time messages
}