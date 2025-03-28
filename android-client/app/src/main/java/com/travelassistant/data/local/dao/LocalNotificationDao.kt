package com.travelassistant.data.local.dao

import androidx.room.*
import com.travelassistant.data.local.entity.LocalNotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalNotificationDao {
    // CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: LocalNotificationEntity)

    @Update
    suspend fun updateNotification(notification: LocalNotificationEntity)

    @Delete
    suspend fun deleteNotification(notification: LocalNotificationEntity)

    // Queries
    @Query("SELECT * FROM local_notifications WHERE id = :notificationId")
    suspend fun getNotificationById(notificationId: String): LocalNotificationEntity?

    @Query("SELECT * FROM local_notifications WHERE userId = :userId")
    fun getNotificationsByUserId(userId: String): Flow<List<LocalNotificationEntity>>

    @Query("SELECT * FROM local_notifications WHERE searchId = :searchId")
    fun getNotificationsBySearchId(searchId: String): Flow<List<LocalNotificationEntity>>

    @Query("SELECT * FROM local_notifications WHERE flightId = :flightId")
    fun getNotificationsByFlightId(flightId: String): Flow<List<LocalNotificationEntity>>

    // Read Status
    @Query("""
        UPDATE local_notifications 
        SET readAt = :timestamp 
        WHERE id = :notificationId
    """)
    suspend fun markAsRead(notificationId: String, timestamp: Long)

    @Query("""
        UPDATE local_notifications 
        SET readAt = NULL 
        WHERE id = :notificationId
    """)
    suspend fun markAsUnread(notificationId: String)

    // Unread Notifications
    @Query("""
        SELECT * FROM local_notifications 
        WHERE userId = :userId 
        AND readAt IS NULL 
        ORDER BY createdAt DESC
    """)
    fun getUnreadNotifications(userId: String): Flow<List<LocalNotificationEntity>>

    // Search-specific Notifications
    @Query("""
        SELECT * FROM local_notifications 
        WHERE searchId = :searchId 
        AND readAt IS NULL 
        ORDER BY createdAt DESC
    """)
    fun getUnreadNotificationsForSearch(searchId: String): Flow<List<LocalNotificationEntity>>

    // Flight-specific Notifications
    @Query("""
        SELECT * FROM local_notifications 
        WHERE flightId = :flightId 
        AND readAt IS NULL 
        ORDER BY createdAt DESC
    """)
    fun getUnreadNotificationsForFlight(flightId: String): Flow<List<LocalNotificationEntity>>

    // Cleanup
    @Query("DELETE FROM local_notifications WHERE userId = :userId")
    suspend fun deleteAllNotificationsForUser(userId: String)

    @Query("DELETE FROM local_notifications WHERE searchId = :searchId")
    suspend fun deleteAllNotificationsForSearch(searchId: String)

    @Query("DELETE FROM local_notifications WHERE flightId = :flightId")
    suspend fun deleteAllNotificationsForFlight(flightId: String)
} 