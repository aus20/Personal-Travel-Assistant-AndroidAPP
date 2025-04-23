package com.travelassistant.data.repository.impl

import com.travelassistant.data.local.dao.LocalNotificationDao
import com.travelassistant.data.local.entity.LocalNotificationEntity
import com.travelassistant.data.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Implementation of the NotificationRepository interface.
 * This class handles both local storage and remote synchronization of notifications.
 */
class NotificationRepositoryImpl(
    private val localNotificationDao: LocalNotificationDao,
    private val isOnline: Boolean = true
) : NotificationRepository {

    override suspend fun insertNotification(notification: LocalNotificationEntity): String {
        // If the notification doesn't have an ID, generate one
        val notificationWithId = if (notification.id.isEmpty()) {
            notification.copy(id = UUID.randomUUID().toString())
        } else {
            notification
        }
        
        localNotificationDao.insertNotification(notificationWithId)
        return notificationWithId.id
    }

    override suspend fun updateNotification(notification: LocalNotificationEntity) {
        localNotificationDao.updateNotification(notification)
    }

    override suspend fun deleteNotification(notification: LocalNotificationEntity) {
        localNotificationDao.deleteNotification(notification)
    }

    override suspend fun getNotificationById(notificationId: String): LocalNotificationEntity? {
        return localNotificationDao.getNotificationById(notificationId)
    }

    override fun getNotificationsByUserId(userId: String): Flow<List<LocalNotificationEntity>> {
        return localNotificationDao.getNotificationsByUserId(userId)
    }

    override fun getNotificationsBySearchId(searchId: String): Flow<List<LocalNotificationEntity>> {
        return localNotificationDao.getNotificationsBySearchId(searchId)
    }

    override fun getNotificationsByFlightId(flightId: String): Flow<List<LocalNotificationEntity>> {
        return localNotificationDao.getNotificationsByFlightId(flightId)
    }

    override suspend fun markAsRead(notificationId: String, timestamp: Long) {
        localNotificationDao.markAsRead(notificationId, timestamp)
    }

    override suspend fun markAsUnread(notificationId: String) {
        localNotificationDao.markAsUnread(notificationId)
    }

    override fun getUnreadNotifications(userId: String): Flow<List<LocalNotificationEntity>> {
        return localNotificationDao.getUnreadNotifications(userId)
    }

    override fun getUnreadNotificationsForSearch(searchId: String): Flow<List<LocalNotificationEntity>> {
        return localNotificationDao.getUnreadNotificationsForSearch(searchId)
    }

    override fun getUnreadNotificationsForFlight(flightId: String): Flow<List<LocalNotificationEntity>> {
        return localNotificationDao.getUnreadNotificationsForFlight(flightId)
    }

    override suspend fun deleteAllNotificationsForUser(userId: String) {
        localNotificationDao.deleteAllNotificationsForUser(userId)
    }

    override suspend fun deleteAllNotificationsForSearch(searchId: String) {
        localNotificationDao.deleteAllNotificationsForSearch(searchId)
    }

    override suspend fun deleteAllNotificationsForFlight(flightId: String) {
        localNotificationDao.deleteAllNotificationsForFlight(flightId)
    }

    override suspend fun syncWithRemote(): Int {
        // This is a placeholder for the actual sync implementation
        // In a real implementation, this would:
        // 1. Get notifications that need to be synced
        // 2. Send them to the remote server
        // 3. Get updates from the remote server
        // 4. Update the local database
        // 5. Return the number of synchronized items
        
        if (!isOnline) {
            return 0
        }
        
        // For now, just return a dummy value
        return 0
    }
} 