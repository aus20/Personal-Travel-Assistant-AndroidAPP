package com.travelassistant.data.sync

import com.travelassistant.data.local.entity.AppPreferencesEntity
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import com.travelassistant.data.local.entity.LocalNotificationEntity
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.data.repository.NotificationRepository
import com.travelassistant.data.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service responsible for synchronizing data between local Room database and remote PostgreSQL database.
 * This service coordinates the sync process across all repositories and handles conflict resolution.
 */
@Singleton
class DatabaseSyncService @Inject constructor(
    private val flightSearchRepository: FlightSearchRepository,
    private val flightResultRepository: FlightResultRepository,
    private val preferencesRepository: PreferencesRepository,
    private val notificationRepository: NotificationRepository,
    private val flightDataSyncStrategy: SyncStrategy,
    private val preferencesSyncStrategy: SyncStrategy,
    private val notificationSyncStrategy: SyncStrategy,
    private val flightSearchConflictStrategy: ConflictResolutionStrategy<SavedFlightSearchEntity>,
    private val flightResultConflictStrategy: ConflictResolutionStrategy<CachedFlightResultEntity>,
    private val preferencesConflictStrategy: ConflictResolutionStrategy<AppPreferencesEntity>,
    private val notificationConflictStrategy: ConflictResolutionStrategy<LocalNotificationEntity>
) {
    private val isSyncing = AtomicBoolean(false)

    /**
     * Performs a full synchronization of all data.
     * @return The number of items synchronized across all repositories
     */
    suspend fun syncAll(): Int = withContext(Dispatchers.IO) {
        if (!isSyncing.compareAndSet(false, true)) {
            return@withContext 0
        }

        try {
            val currentTime = System.currentTimeMillis()
            var totalCount = 0

            // Sync flight data if strategy allows
            if (flightDataSyncStrategy.shouldSync(
                    flightSearchRepository.getSearchesNeedingSync(currentTime).firstOrNull()?.lastSyncedAt ?: 0L,
                    currentTime
                )) {
                totalCount += syncFlightData()
            }

            // Sync preferences if strategy allows
            if (preferencesSyncStrategy.shouldSync(
                    preferencesRepository.getPreferencesNeedingSync(currentTime).firstOrNull()?.lastSyncTimestamp ?: 0L,
                    currentTime
                )) {
                totalCount += syncPreferences()
            }

            // Sync notifications if strategy allows
            if (notificationSyncStrategy.shouldSync(0L, currentTime)) {
                totalCount += syncNotifications()
            }

            totalCount
        } finally {
            isSyncing.set(false)
        }
    }

    /**
     * Synchronizes flight searches and their associated results.
     * @return The number of items synchronized
     */
    suspend fun syncFlightData(): Int = withContext(Dispatchers.IO) {
        if (!isSyncing.compareAndSet(false, true)) {
            return@withContext 0
        }

        try {
            val searchCount = flightSearchRepository.syncWithRemote()
            val resultCount = flightResultRepository.syncWithRemote()
            searchCount + resultCount
        } finally {
            isSyncing.set(false)
        }
    }

    /**
     * Synchronizes user preferences.
     * @return The number of items synchronized
     */
    suspend fun syncPreferences(): Int = withContext(Dispatchers.IO) {
        if (!isSyncing.compareAndSet(false, true)) {
            return@withContext 0
        }

        try {
            preferencesRepository.syncWithRemote()
        } finally {
            isSyncing.set(false)
        }
    }

    /**
     * Synchronizes notifications.
     * @return The number of items synchronized
     */
    suspend fun syncNotifications(): Int = withContext(Dispatchers.IO) {
        if (!isSyncing.compareAndSet(false, true)) {
            return@withContext 0
        }

        try {
            notificationRepository.syncWithRemote()
        } finally {
            isSyncing.set(false)
        }
    }

    /**
     * Checks if a sync operation is currently in progress.
     * @return true if syncing, false otherwise
     */
    fun isSyncing(): Boolean = isSyncing.get()

    /**
     * Resolves conflicts between local and remote data.
     * This implementation uses the appropriate conflict resolution strategy for each data type.
     */
    private suspend fun resolveConflicts() {
        val currentTime = System.currentTimeMillis()

        // Resolve flight search conflicts
        val searchesNeedingSync = flightSearchRepository.getSearchesNeedingSync(currentTime)
        for (search in searchesNeedingSync) {
            // In a real implementation, we would get the remote version and resolve the conflict
            val remoteVersion = search // Placeholder
            val resolvedVersion = flightSearchConflictStrategy.resolveConflict(search, remoteVersion)
            flightSearchRepository.updateSearch(resolvedVersion)
        }

        // Resolve flight result conflicts
        val resultsNeedingSync = flightResultRepository.getExpiredResults(currentTime)
        for (result in resultsNeedingSync) {
            // In a real implementation, we would get the remote version and resolve the conflict
            val remoteVersion = result // Placeholder
            val resolvedVersion = flightResultConflictStrategy.resolveConflict(result, remoteVersion)
            flightResultRepository.updateFlightResult(resolvedVersion)
        }

        // Resolve preferences conflicts
        val preferencesNeedingSync = preferencesRepository.getPreferencesNeedingSync(currentTime)
        for (preferences in preferencesNeedingSync) {
            // In a real implementation, we would get the remote version and resolve the conflict
            val remoteVersion = preferences // Placeholder
            val resolvedVersion = preferencesConflictStrategy.resolveConflict(preferences, remoteVersion)
            preferencesRepository.updatePreferences(resolvedVersion)
        }

        // Resolve notification conflicts
        // In a real implementation, we would get the remote notifications and resolve conflicts
    }
} 