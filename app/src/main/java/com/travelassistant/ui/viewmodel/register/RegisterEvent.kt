package com.travelassistant.ui.viewmodel.register

sealed class RegisterEvent {
    data class NameChanged(val name: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    data object SignUpClicked : RegisterEvent()
}