package com.travelassistant.data.sync

import javax.inject.Inject

/**
 * Interface defining conflict resolution strategies for different types of data.
 */
interface ConflictResolutionStrategy<T> {
    /**
     * Resolves a conflict between local and remote versions of data.
     * @param localVersion The local version of the data
     * @param remoteVersion The remote version of the data
     * @return The resolved version of the data
     */
    suspend fun resolveConflict(localVersion: T, remoteVersion: T): T
}

/**
 * Last-write-wins strategy that always uses the most recently updated version.
 */
class LastWriteWinsStrategy<T> @Inject constructor() : ConflictResolutionStrategy<T> {
    override suspend fun resolveConflict(localVersion: T, remoteVersion: T): T {
        // This is a simplified version. In a real implementation, we would compare timestamps
        // and return the most recent version.
        return remoteVersion
    }
}

/**
 * Merge strategy that combines changes from both versions.
 */
class MergeStrategy<T> @Inject constructor() : ConflictResolutionStrategy<T> {
    override suspend fun resolveConflict(localVersion: T, remoteVersion: T): T {
        // This is a placeholder. In a real implementation, we would merge the changes
        // based on the specific data type.
        return remoteVersion
    }
}

/**
 * Local-wins strategy that always uses the local version.
 */
class LocalWinsStrategy<T> @Inject constructor() : ConflictResolutionStrategy<T> {
    override suspend fun resolveConflict(localVersion: T, remoteVersion: T): T {
        return localVersion
    }
}

/**
 * Remote-wins strategy that always uses the remote version.
 */
class RemoteWinsStrategy<T> @Inject constructor() : ConflictResolutionStrategy<T> {
    override suspend fun resolveConflict(localVersion: T, remoteVersion: T): T {
        return remoteVersion
    }
}

/**
 * Custom strategy that uses a provided function to resolve conflicts.
 */
class CustomStrategy<T> @Inject constructor(
    private val resolveFunction: suspend (T, T) -> T
) : ConflictResolutionStrategy<T> {
    override suspend fun resolveConflict(localVersion: T, remoteVersion: T): T {
        return resolveFunction(localVersion, remoteVersion)
    }
} 