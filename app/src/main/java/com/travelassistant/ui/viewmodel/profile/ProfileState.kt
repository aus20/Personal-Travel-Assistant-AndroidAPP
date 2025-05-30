package com.travelassistant.ui.viewmodel.profile

import java.util.Date

sealed class ProfileState {
    data object Initial : ProfileState()
    data object Loading : ProfileState()
    data class Error(val message: String) : ProfileState()
    data class Success(
        val userProfile: UserProfile,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : ProfileState()
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val preferences: UserPreferences,
    val createdAt: Date = Date(),
    val lastUpdated: Date = Date()
)

data class UserPreferences(
    val notificationSettings: NotificationSettings,
    val searchPreferences: SearchPreferences,
    val displayPreferences: DisplayPreferences,
    val syncPreferences: SyncPreferences
)

data class NotificationSettings(
    val priceAlerts: Boolean = true,
    val flightStatusUpdates: Boolean = true,
    val bookingConfirmations: Boolean = true,
    val promotionalOffers: Boolean = false,
    val emailNotifications: Boolean = true,
    val pushNotifications: Boolean = true
)

data class SearchPreferences(
    val defaultPassengerCount: Int = 1,
    val preferredAirlines: Set<String> = emptySet(),
    val maxStops: Int = 2,
    val priceRange: ClosedFloatingPointRange<Float> = 0f..Float.MAX_VALUE,
    val sortBy: SortOption = SortOption.PRICE_LOW_TO_HIGH
)

data class DisplayPreferences(
    val theme: Theme = Theme.SYSTEM,
    val language: String = "en",
    val currency: String = "USD",
    val dateFormat: String = "MM/dd/yyyy",
    val timeFormat: String = "12h"
)

data class SyncPreferences(
    val autoSync: Boolean = true,
    val syncFrequency: SyncFrequency = SyncFrequency.DAILY,
    val syncOnWifiOnly: Boolean = true,
    val syncOnCharging: Boolean = false
)

enum class Theme {
    LIGHT,
    DARK,
    SYSTEM
}

enum class SortOption {
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    DURATION_SHORT_TO_LONG,
    DEPARTURE_TIME_EARLY_TO_LATE
}

enum class SyncFrequency {
    HOURLY,
    DAILY,
    WEEKLY,
    MANUAL
} 