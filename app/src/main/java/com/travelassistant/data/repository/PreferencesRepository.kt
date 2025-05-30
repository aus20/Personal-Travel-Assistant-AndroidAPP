package com.travelassistant.data.repository

import com.travelassistant.data.local.entity.AppPreferencesEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for app preferences operations.
 * This repository handles both local storage and remote synchronization of app preferences.
 */
interface PreferencesRepository {
    /**
     * Insert new app preferences into the local database.
     * @param preferences The app preferences to insert
     */
    suspend fun insertPreferences(preferences: AppPreferencesEntity)

    /**
     * Update existing app preferences in the local database.
     * @param preferences The app preferences to update
     */
    suspend fun updatePreferences(preferences: AppPreferencesEntity)

    /**
     * Delete app preferences from the local database.
     * @param preferences The app preferences to delete
     */
    suspend fun deletePreferences(preferences: AppPreferencesEntity)

    /**
     * Get app preferences for a specific user.
     * @param userId The ID of the user
     * @return The app preferences, or null if not found
     */
    suspend fun getPreferencesByUserId(userId: String): AppPreferencesEntity?

    /**
     * Get app preferences for a specific user as a Flow.
     * @param userId The ID of the user
     * @return A Flow of app preferences that will emit updates when the data changes
     */
    fun getPreferencesByUserIdFlow(userId: String): Flow<List<AppPreferencesEntity?>>

    /**
     * Update the notification status for a specific user.
     * @param userId The ID of the user
     * @param enabled Whether notifications are enabled
     */
    suspend fun updateNotificationStatus(userId: String, enabled: Boolean)

    /**
     * Update the price alert threshold for a specific user.
     * @param userId The ID of the user
     * @param threshold The price alert threshold
     */
    suspend fun updatePriceAlertThreshold(userId: String, threshold: Double)

    /**
     * Update the preferred currency for a specific user.
     * @param userId The ID of the user
     * @param currency The preferred currency
     */
    suspend fun updatePreferredCurrency(userId: String, currency: String)

    /**
     * Update the last sync timestamp for a specific user.
     * @param userId The ID of the user
     * @param timestamp The last sync timestamp
     */
    suspend fun updateLastSyncTimestamp(userId: String, timestamp: Long)

    /**
     * Get app preferences that need to be synchronized with the remote database.
     * @param timestamp The timestamp to compare against
     * @return A list of app preferences that need to be synchronized
     */
    suspend fun getPreferencesNeedingSync(timestamp: Long): List<AppPreferencesEntity>

    /**
     * Create default app preferences for a specific user.
     * @param userId The ID of the user
     * @param timestamp The current timestamp
     */
    suspend fun createDefaultPreferences(userId: String, timestamp: Long)

    /**
     * Synchronize app preferences with the remote database.
     * @return The number of synchronized items
     */
    suspend fun syncWithRemote(): Int
} 