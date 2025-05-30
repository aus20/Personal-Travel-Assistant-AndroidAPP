package com.travelassistant.data.sync

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

/**
 * Interface defining sync strategies for different types of data.
 */
interface SyncStrategy {
    /**
     * Determines if sync should be performed based on the current conditions.
     * @param lastSyncTime The timestamp of the last successful sync
     * @param currentTime The current timestamp
     * @return true if sync should be performed, false otherwise
     */
    fun shouldSync(lastSyncTime: Long, currentTime: Long): Boolean

    /**
     * Gets the priority of this sync strategy.
     * @return The priority (higher number = higher priority)
     */
    fun getPriority(): Int
}

/**
 * Time-based sync strategy that syncs based on elapsed time since last sync.
 */
class TimeBasedSyncStrategy @Inject constructor(
    private val syncIntervalMillis: Long
) : SyncStrategy {
    override fun shouldSync(lastSyncTime: Long, currentTime: Long): Boolean {
        return currentTime - lastSyncTime >= syncIntervalMillis
    }

    override fun getPriority(): Int = 1
}

/**
 * Network-aware sync strategy that considers network conditions.
 */
class NetworkAwareSyncStrategy @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val requireWifi: Boolean = false
) : SyncStrategy {
    override fun shouldSync(lastSyncTime: Long, currentTime: Long): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return if (requireWifi) {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
    }

    override fun getPriority(): Int = 2
}

/**
 * Priority-based sync strategy that syncs based on data importance.
 */
class PriorityBasedSyncStrategy @Inject constructor(
    private val priority: Int
) : SyncStrategy {
    override fun shouldSync(lastSyncTime: Long, currentTime: Long): Boolean {
        // Always sync high-priority data
        return priority >= HIGH_PRIORITY
    }

    override fun getPriority(): Int = priority

    companion object {
        const val HIGH_PRIORITY = 3
        const val MEDIUM_PRIORITY = 2
        const val LOW_PRIORITY = 1
    }
}

/**
 * Composite sync strategy that combines multiple strategies.
 */
class CompositeSyncStrategy @Inject constructor(
    private val strategies: List<SyncStrategy>
) : SyncStrategy {
    override fun shouldSync(lastSyncTime: Long, currentTime: Long): Boolean {
        return strategies.any { it.shouldSync(lastSyncTime, currentTime) }
    }

    override fun getPriority(): Int {
        return strategies.maxOf { it.getPriority() }
    }
} 