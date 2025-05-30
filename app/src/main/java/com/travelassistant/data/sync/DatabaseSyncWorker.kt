package com.travelassistant.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import javax.inject.Inject

/**
 * Worker class for handling background database synchronization.
 * This worker is responsible for performing sync operations in the background.
 */
class DatabaseSyncWorker @Inject constructor(
    context: Context,
    params: WorkerParameters,
    private val databaseSyncService: DatabaseSyncService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Perform the sync operation
            val syncCount = databaseSyncService.syncAll()

            // Return success if items were synced, or if no sync was needed
            if (syncCount >= 0) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            // Return retry for transient errors, failure for permanent errors
            if (isTransientError(e)) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    /**
     * Determines if an error is transient and can be retried.
     * @param e The exception to check
     * @return true if the error is transient, false otherwise
     */
    private fun isTransientError(e: Exception): Boolean {
        return when (e) {
            is java.net.UnknownHostException,
            is java.net.SocketTimeoutException,
            is java.io.IOException -> true
            else -> false
        }
    }
} 