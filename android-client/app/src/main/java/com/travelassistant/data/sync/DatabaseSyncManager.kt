package com.travelassistant.data.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager class for handling database synchronization scheduling.
 * This class is responsible for configuring and scheduling sync workers.
 */
@Singleton
class DatabaseSyncManager @Inject constructor(
    private val context: Context,
    private val workManager: WorkManager
) {
    companion object {
        private const val SYNC_WORK_NAME = "database_sync"
        private const val SYNC_INTERVAL_HOURS = 1L
    }

    /**
     * Schedules periodic database synchronization.
     * This method configures the sync worker with appropriate constraints and scheduling.
     */
    fun schedulePeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<DatabaseSyncWorker>(
            SYNC_INTERVAL_HOURS, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            syncWorkRequest
        )
    }

    /**
     * Cancels all scheduled sync operations.
     */
    fun cancelSync() {
        workManager.cancelUniqueWork(SYNC_WORK_NAME)
    }
} 