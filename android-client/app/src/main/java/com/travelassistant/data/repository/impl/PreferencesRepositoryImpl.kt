package com.travelassistant.data.repository.impl

import com.travelassistant.data.local.dao.AppPreferencesDao
import com.travelassistant.data.local.entity.AppPreferencesEntity
import com.travelassistant.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the PreferencesRepository interface.
 * This class handles both local storage and remote synchronization of app preferences.
 */
class PreferencesRepositoryImpl(
    private val appPreferencesDao: AppPreferencesDao,
    private val isOnline: Boolean = true
) : PreferencesRepository {

    override suspend fun insertPreferences(preferences: AppPreferencesEntity) {
        appPreferencesDao.insertPreferences(preferences)
    }

    override suspend fun updatePreferences(preferences: AppPreferencesEntity) {
        appPreferencesDao.updatePreferences(preferences)
    }

    override suspend fun deletePreferences(preferences: AppPreferencesEntity) {
        appPreferencesDao.deletePreferences(preferences)
    }

    override suspend fun getPreferencesByUserId(userId: String): AppPreferencesEntity? {
        return appPreferencesDao.getPreferencesByUserId(userId)
    }

    override fun getPreferencesByUserIdFlow(userId: String): Flow<List<AppPreferencesEntity?>> {
        return appPreferencesDao.getPreferencesByUserIdFlow(userId)
    }

    override suspend fun updateNotificationStatus(userId: String, enabled: Boolean) {
        appPreferencesDao.updateNotificationStatus(userId, enabled)
    }

    override suspend fun updatePriceAlertThreshold(userId: String, threshold: Double) {
        appPreferencesDao.updatePriceAlertThreshold(userId, threshold)
    }

    override suspend fun updatePreferredCurrency(userId: String, currency: String) {
        appPreferencesDao.updatePreferredCurrency(userId, currency)
    }

    override suspend fun updateLastSyncTimestamp(userId: String, timestamp: Long) {
        appPreferencesDao.updateLastSyncTimestamp(userId, timestamp)
    }

    override suspend fun getPreferencesNeedingSync(timestamp: Long): List<AppPreferencesEntity> {
        return appPreferencesDao.getPreferencesNeedingSync(timestamp)
    }

    override suspend fun createDefaultPreferences(userId: String, timestamp: Long) {
        val defaultPreferences = AppPreferencesEntity(
            userId = userId,
            notificationEnabled = true,
            priceAlertThreshold = 10.0,
            preferredCurrency = "USD",
            lastSyncTimestamp = timestamp,
            language = "English"
        )
        insertPreferences(defaultPreferences)
    }

    override suspend fun syncWithRemote(): Int {
        // This is a placeholder for the actual sync implementation
        // In a real implementation, this would:
        // 1. Get preferences that need to be synced
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