package com.travelassistant.ui.viewmodel.notifications

sealed class NotificationEvent {
    data class MarkAsRead(val notificationId: String) : NotificationEvent()
    data class MarkAsUnread(val notificationId: String) : NotificationEvent()
    data class DeleteNotification(val notificationId: String) : NotificationEvent()
    data object RefreshNotifications : NotificationEvent()
    data class ViewFlight(val flightId: String) : NotificationEvent()
    data class ViewSearch(val searchId: String) : NotificationEvent()
} 