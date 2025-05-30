package com.travelassistant.data.repository.impl

import com.travelassistant.data.local.dao.CachedFlightResultDao
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.data.network.NetworkStateManager
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of the FlightResultRepository interface.
 * This class handles both local storage and remote synchronization of flight results.
 */

@Singleton
class FlightResultRepositoryImpl @Inject constructor(
    private val cachedFlightResultDao: CachedFlightResultDao,
    private val networkStateManager: NetworkStateManager
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

    override suspend fun getFlightResultsBySearchId(searchId: String): List<CachedFlightResultEntity>? {
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
        if (!networkStateManager.isOnline.value) { // Use networkStateManager
            return 0
        }
        // ... rest of sync logic ...
        return 0
    }
} 