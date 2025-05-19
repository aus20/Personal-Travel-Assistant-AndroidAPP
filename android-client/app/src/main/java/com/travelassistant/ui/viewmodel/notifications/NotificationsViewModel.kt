package com.travelassistant.ui.viewmodel.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.NotificationRepository
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkStateManager: NetworkStateManager
) : BaseUiViewModel<NotificationsState, NotificationEvent>() {

    override val _state = MutableStateFlow<UiState<NotificationsState>>(UiState.Initial)

    private val _notificationsState = MutableStateFlow<NotificationsState>(NotificationsState.Initial)
    val notificationsState: StateFlow<NotificationsState> = _notificationsState.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        viewModelScope.launch {
            networkStateManager.isOnline.collect { isOnline ->
                _isOnline.value = isOnline
            }
        }
        loadNotifications()
    }

    override fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.MarkAsRead -> markAsRead(event.notificationId)
            is NotificationEvent.MarkAsUnread -> markAsUnread(event.notificationId)
            is NotificationEvent.DeleteNotification -> deleteNotification(event.notificationId)
            is NotificationEvent.RefreshNotifications -> loadNotifications()
            is NotificationEvent.ViewFlight -> handleViewFlight(event.flightId)
            is NotificationEvent.ViewSearch -> handleViewSearch(event.searchId)
        }
    }

    private fun loadNotifications() {
        handleResult(
            block = {
                // TODO: Get current user ID from auth manager
                val userId = "current_user"
                notificationRepository.getNotificationsByUserId(userId)
            },
            onSuccess = { notifications ->
                updateState { currentState ->
                    val notificationList = notifications.map { entity ->
                        Notification(
                            id = entity.id,
                            type = determineNotificationType(entity),
                            title = entity.title,
                            message = entity.message,
                            isRead = entity.readAt != null,
                            createdAt = Date(entity.createdAt),
                            action = determineNotificationAction(entity)
                        )
                    }
                    UiState.Success(NotificationsState.Success(notifications = notificationList))
                }
            }
        )
    }

    private fun markAsRead(notificationId: String) {
        handleResult(
            block = {
                notificationRepository.markAsRead(notificationId, System.currentTimeMillis())
            },
            onSuccess = {
                loadNotifications()
            }
        )
    }

    private fun markAsUnread(notificationId: String) {
        handleResult(
            block = {
                notificationRepository.markAsUnread(notificationId)
            },
            onSuccess = {
                loadNotifications()
            }
        )
    }

    private fun deleteNotification(notificationId: String) {
        handleResult(
            block = {
                val notification = notificationRepository.getNotificationById(notificationId)
                notification?.let {
                    notificationRepository.deleteNotification(it)
                }
            },
            onSuccess = {
                loadNotifications()
            }
        )
    }

    private fun handleViewFlight(flightId: String) {
        // TODO: Navigate to flight details screen
    }

    private fun handleViewSearch(searchId: String) {
        // TODO: Navigate to search results screen
    }

    private fun determineNotificationType(entity: LocalNotificationEntity): NotificationType {
        return when {
            entity.title.contains("price", ignoreCase = true) -> NotificationType.PRICE_ALERT
            entity.title.contains("status", ignoreCase = true) -> NotificationType.FLIGHT_STATUS
            entity.title.contains("booking", ignoreCase = true) -> NotificationType.BOOKING_CONFIRMATION
            else -> NotificationType.GENERAL
        }
    }

    private fun determineNotificationAction(entity: LocalNotificationEntity): NotificationAction? {
        return when {
            entity.flightId.isNotBlank() -> NotificationAction.ViewFlight(entity.flightId)
            entity.searchId.isNotBlank() -> NotificationAction.ViewSearch(entity.searchId)
            else -> null
        }
    }
} 