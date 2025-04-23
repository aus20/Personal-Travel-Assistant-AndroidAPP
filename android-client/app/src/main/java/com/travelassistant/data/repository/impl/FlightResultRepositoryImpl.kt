package com.travelassistant.data.repository.impl

import com.travelassistant.data.local.dao.CachedFlightResultDao
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import com.travelassistant.data.repository.FlightResultRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Implementation of the FlightResultRepository interface.
 * This class handles both local storage and remote synchronization of flight results.
 */
class FlightResultRepositoryImpl(
    private val cachedFlightResultDao: CachedFlightResultDao,
    private val isOnline: Boolean = true
) : FlightResultRepository {

    override suspend fun insertFlightResult(result: CachedFlightResultEntity): String {
        // If the result doesn't have an ID, generate one
        val resultWithId = if (result.id.isEmpty()) {
            result.copy(id = UUID.randomUUID().toString())
        } else {
            result
        }
        
        cachedFlightResultDao.insertFlightResult(resultWithId)
        return resultWithId.id
    }

    override suspend fun updateFlightResult(result: CachedFlightResultEntity) {
        cachedFlightResultDao.updateFlightResult(result)
    }

    override suspend fun deleteFlightResult(result: CachedFlightResultEntity) {
        cachedFlightResultDao.deleteFlightResult(result)
    }

    override suspend fun getFlightResultById(flightId: String): CachedFlightResultEntity? {
        return cachedFlightResultDao.getFlightResultById(flightId)
    }

    override fun getFlightResultsBySearchId(searchId: String): Flow<List<CachedFlightResultEntity>> {
        return cachedFlightResultDao.getFlightResultsBySearchId(searchId)
    }

    override suspend fun getExpiredResults(currentTime: Long): List<CachedFlightResultEntity> {
        return cachedFlightResultDao.getExpiredResults(currentTime)
    }

    override fun getMatchingFlights(
        searchId: String,
        maxPrice: Double,
        maxStops: Int
    ): Flow<List<CachedFlightResultEntity>> {
        return cachedFlightResultDao.getMatchingFlights(searchId, maxPrice, maxStops)
    }

    override fun getFlightsInTimeRange(
        searchId: String,
        startTime: Long,
        endTime: Long
    ): Flow<List<CachedFlightResultEntity>> {
        return cachedFlightResultDao.getFlightsInTimeRange(searchId, startTime, endTime)
    }

    override fun getPriceDrops(
        searchId: String,
        priceThreshold: Double,
        previousPrice: Double
    ): Flow<List<CachedFlightResultEntity>> {
        return cachedFlightResultDao.getPriceDrops(searchId, priceThreshold, previousPrice)
    }

    override suspend fun deleteExpiredResults(currentTime: Long) {
        cachedFlightResultDao.deleteExpiredResults(currentTime)
    }

    override suspend fun deleteResultsBySearchId(searchId: String) {
        cachedFlightResultDao.deleteResultsBySearchId(searchId)
    }

    override suspend fun syncWithRemote(): Int {
        // This is a placeholder for the actual sync implementation
        // In a real implementation, this would:
        // 1. Get results that need to be synced
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