package com.travelassistant.ui.viewmodel.notifications

import com.travelassistant.data.model.FlightResult
import java.util.Date

sealed class NotificationsState {
    data object Initial : NotificationsState()
    data object Loading : NotificationsState()
    data class Error(val message: String) : NotificationsState()
    data class Success(
        val notifications: List<Notification>,
        val isLoading: Boolean = false,
        val error: String? = null
    ) : NotificationsState()
}

data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val flightResult: FlightResult? = null,
    val isRead: Boolean = false,
    val createdAt: Date = Date(),
    val action: NotificationAction? = null
)

enum class NotificationType {
    PRICE_ALERT,
    FLIGHT_STATUS,
    BOOKING_CONFIRMATION,
    GENERAL
}

sealed class NotificationAction {
    data class ViewFlight(val flightId: String) : NotificationAction()
    data class ViewBooking(val bookingId: String) : NotificationAction()
    data class ViewSearch(val searchId: String) : NotificationAction()
    data object Dismiss : NotificationAction()
} 