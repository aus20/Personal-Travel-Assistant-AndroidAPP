package com.travelassistant.data.repository.impl

import com.travelassistant.data.local.dao.SavedFlightSearchDao
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.network.NetworkStateManager
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the FlightSearchRepository interface.
 * This class handles both local storage and remote synchronization of flight searches.
 */

@Singleton
class FlightSearchRepositoryImpl @Inject constructor(
    private val savedFlightSearchDao: SavedFlightSearchDao,
    private val networkStateManager: NetworkStateManager
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
        if (!networkStateManager.isOnline.value) { // Use networkStateManager
            return 0
        }
        // ... rest of sync logic ...
        return 0
    }

    override suspend fun getSearchesNeedingSync(timestamp: Long): List<SavedFlightSearchEntity> {
        return savedFlightSearchDao.getSearchesNeedingSync(timestamp)
    }
} 