package com.travelassistant.data.repository.impl

import com.travelassistant.data.local.dao.SavedFlightSearchDao
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.repository.FlightSearchRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Implementation of the FlightSearchRepository interface.
 * This class handles both local storage and remote synchronization of flight searches.
 */
class FlightSearchRepositoryImpl(
    private val savedFlightSearchDao: SavedFlightSearchDao,
    private val isOnline: Boolean = true
) : FlightSearchRepository {

    override suspend fun insertSearch(search: SavedFlightSearchEntity): String {
        // If the search doesn't have an ID, generate one
        val searchWithId = if (search.id.isEmpty()) {
            search.copy(id = UUID.randomUUID().toString())
        } else {
            search
        }
        
        savedFlightSearchDao.insertSearch(searchWithId)
        return searchWithId.id
    }

    override suspend fun updateSearch(search: SavedFlightSearchEntity) {
        savedFlightSearchDao.updateSearch(search)
    }

    override suspend fun deleteSearch(search: SavedFlightSearchEntity) {
        savedFlightSearchDao.deleteSearch(search)
    }

    override suspend fun getSearchById(searchId: String): SavedFlightSearchEntity? {
        return savedFlightSearchDao.getSearchById(searchId)
    }

    override fun getSearchesByUserId(userId: String): Flow<List<SavedFlightSearchEntity>> {
        return savedFlightSearchDao.getSearchesByUserId(userId)
    }

    override fun getActiveSearches(userId: String, currentTime: Long): Flow<List<SavedFlightSearchEntity>> {
        return savedFlightSearchDao.getActiveSearches(userId, currentTime)
    }

    override fun getUpcomingSearches(userId: String, currentTime: Long): Flow<List<SavedFlightSearchEntity>> {
        return savedFlightSearchDao.getUpcomingSearches(userId, currentTime)
    }

    override suspend fun deleteExpiredSearches(currentTime: Long) {
        savedFlightSearchDao.deleteExpiredSearches(currentTime)
    }

    override suspend fun syncWithRemote(): Int {
        // This is a placeholder for the actual sync implementation
        // In a real implementation, this would:
        // 1. Get searches that need to be synced
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

    override suspend fun getSearchesNeedingSync(timestamp: Long): List<SavedFlightSearchEntity> {
        return savedFlightSearchDao.getSearchesNeedingSync(timestamp)
    }
} 