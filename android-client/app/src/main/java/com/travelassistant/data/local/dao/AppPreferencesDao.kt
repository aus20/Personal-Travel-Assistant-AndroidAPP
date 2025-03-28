package com.travelassistant.data.local.dao

import androidx.room.*
import com.travelassistant.data.local.entity.AppPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppPreferencesDao {
    // CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: AppPreferencesEntity)

    @Update
    suspend fun updatePreferences(preferences: AppPreferencesEntity)

    @Delete
    suspend fun deletePreferences(preferences: AppPreferencesEntity)

    // Queries
    @Query("SELECT * FROM app_preferences WHERE userId = :userId")
    suspend fun getPreferencesByUserId(userId: String): AppPreferencesEntity?

    @Query("SELECT * FROM app_preferences WHERE userId = :userId")
    fun getPreferencesByUserIdFlow(userId: String): Flow<AppPreferencesEntity?>

    // Notification Settings
    @Query("""
        UPDATE app_preferences 
        SET notificationEnabled = :enabled 
        WHERE userId = :userId
    """)
    suspend fun updateNotificationStatus(userId: String, enabled: Boolean)

    @Query("""
        UPDATE app_preferences 
        SET priceAlertThreshold = :threshold 
        WHERE userId = :userId
    """)
    suspend fun updatePriceAlertThreshold(userId: String, threshold: Double)

    // Currency Preferences
    @Query("""
        UPDATE app_preferences 
        SET preferredCurrency = :currency 
        WHERE userId = :userId
    """)
    suspend fun updatePreferredCurrency(userId: String, currency: String)

    // Sync Status
    @Query("""
        UPDATE app_preferences 
        SET lastSyncTimestamp = :timestamp 
        WHERE userId = :userId
    """)
    suspend fun updateLastSyncTimestamp(userId: String, timestamp: Long)

    @Query("""
        SELECT * FROM app_preferences 
        WHERE lastSyncTimestamp < :timestamp
    """)
    suspend fun getPreferencesNeedingSync(timestamp: Long): List<AppPreferencesEntity>

    // Default Preferences
    @Query("""
        INSERT INTO app_preferences (
            userId, 
            notificationEnabled, 
            priceAlertThreshold, 
            preferredCurrency, 
            lastSyncTimestamp
        ) VALUES (
            :userId,
            true,
            100.0,
            'USD',
            :timestamp
        )
    """)
    suspend fun createDefaultPreferences(userId: String, timestamp: Long)
} 