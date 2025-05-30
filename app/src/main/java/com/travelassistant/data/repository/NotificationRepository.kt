package com.travelassistant.data.repository

import com.travelassistant.data.local.entity.LocalNotificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for notification operations.
 * This repository handles both local storage and remote synchronization of notifications.
 */
interface NotificationRepository {
    /**
     * Insert a new notification into the local database.
     * @param notification The notification to insert
     * @return The ID of the inserted notification
     */
    suspend fun insertNotification(notification: LocalNotificationEntity): String

    /**
     * Update an existing notification in the local database.
     * @param notification The notification to update
     */
    suspend fun updateNotification(notification: LocalNotificationEntity)

    /**
     * Delete a notification from the local database.
     * @param notification The notification to delete
     */
    suspend fun deleteNotification(notification: LocalNotificationEntity)

    /**
     * Get a notification by its ID.
     * @param notificationId The ID of the notification to retrieve
     * @return The notification, or null if not found
     */
    suspend fun getNotificationById(notificationId: String): LocalNotificationEntity?

    /**
     * Get all notifications for a specific user.
     * @param userId The ID of the user
     * @return A Flow of notifications that will emit updates when the data changes
     */
    fun getNotificationsByUserId(userId: String): Flow<List<LocalNotificationEntity>>

    /**
     * Get all notifications for a specific search.
     * @param searchId The ID of the search
     * @return A Flow of notifications that will emit updates when the data changes
     */
    fun getNotificationsBySearchId(searchId: String): Flow<List<LocalNotificationEntity>>

    /**
     * Get all notifications for a specific flight.
     * @param flightId The ID of the flight
     * @return A Flow of notifications that will emit updates when the data changes
     */
    fun getNotificationsByFlightId(flightId: String): Flow<List<LocalNotificationEntity>>

    /**
     * Mark a notification as read.
     * @param notificationId The ID of the notification
     * @param timestamp The timestamp when the notification was read
     */
    suspend fun markAsRead(notificationId: String, timestamp: Long)

    /**
     * Mark a notification as unread.
     * @param notificationId The ID of the notification
     */
    suspend fun markAsUnread(notificationId: String)

    /**
     * Get all unread notifications for a specific user.
     * @param userId The ID of the user
     * @return A Flow of unread notifications that will emit updates when the data changes
     */
    fun getUnreadNotifications(userId: String): Flow<List<LocalNotificationEntity>>

    /**
     * Get all unread notifications for a specific search.
     * @param searchId The ID of the search
     * @return A Flow of unread notifications that will emit updates when the data changes
     */
    fun getUnreadNotificationsForSearch(searchId: String): Flow<List<LocalNotificationEntity>>

    /**
     * Get all unread notifications for a specific flight.
     * @param flightId The ID of the flight
     * @return A Flow of unread notifications that will emit updates when the data changes
     */
    fun getUnreadNotificationsForFlight(flightId: String): Flow<List<LocalNotificationEntity>>

    /**
     * Delete all notifications for a specific user.
     * @param userId The ID of the user
     */
    suspend fun deleteAllNotificationsForUser(userId: String)

    /**
     * Delete all notifications for a specific search.
     * @param searchId The ID of the search
     */
    suspend fun deleteAllNotificationsForSearch(searchId: String)

    /**
     * Delete all notifications for a specific flight.
     * @param flightId The ID of the flight
     */
    suspend fun deleteAllNotificationsForFlight(flightId: String)

    /**
     * Synchronize notifications with the remote database.
     * @return The number of synchronized items
     */
    suspend fun syncWithRemote(): Int
} 