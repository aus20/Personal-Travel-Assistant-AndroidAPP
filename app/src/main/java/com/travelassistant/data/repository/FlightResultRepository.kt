package com.travelassistant.data.repository

import com.travelassistant.data.local.entity.CachedFlightResultEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for flight result operations.
 * This repository handles both local storage and remote synchronization of flight results.
 */
interface FlightResultRepository {
    /**
     * Insert a new flight result into the local database.
     * @param result The flight result to insert
     * @return The ID of the inserted result
     */
    suspend fun insertFlightResult(result: CachedFlightResultEntity): String

    /**
     * Update an existing flight result in the local database.
     * @param result The flight result to update
     */
    suspend fun updateFlightResult(result: CachedFlightResultEntity)

    /**
     * Delete a flight result from the local database.
     * @param result The flight result to delete
     */
    suspend fun deleteFlightResult(result: CachedFlightResultEntity)

    /**
     * Get a flight result by its ID.
     * @param flightId The ID of the flight result to retrieve
     * @return The flight result, or null if not found
     */
    suspend fun getFlightResultById(flightId: String): CachedFlightResultEntity?

    /**
     * Get all flight results for a specific search.
     * @param searchId The ID of the search
     * @return A Flow of flight results that will emit updates when the data changes
     */
    suspend fun getFlightResultsBySearchId(searchId: String): List<CachedFlightResultEntity>?

    /**
     * Get all expired flight results.
     * @param currentTime The current timestamp
     * @return A list of expired flight results
     */
    suspend fun getExpiredResults(currentTime: Long): List<CachedFlightResultEntity>

    /**
     * Get all flight results that match the search criteria.
     * @param searchId The ID of the search
     * @param maxPrice The maximum price
     * @param maxStops The maximum number of stops
     * @return A Flow of matching flight results that will emit updates when the data changes
     */
    fun getMatchingFlights(
        searchId: String,
        maxPrice: Double,
        maxStops: Int
    ): Flow<List<CachedFlightResultEntity>>

    /**
     * Get all flight results within a specific time range.
     * @param searchId The ID of the search
     * @param startTime The start time
     * @param endTime The end time
     * @return A Flow of flight results within the time range that will emit updates when the data changes
     */
    fun getFlightsInTimeRange(
        searchId: String,
        startTime: Long,
        endTime: Long
    ): Flow<List<CachedFlightResultEntity>>

    /**
     * Get all flight results with price drops.
     * @param searchId The ID of the search
     * @param priceThreshold The price threshold
     * @param previousPrice The previous price
     * @return A Flow of flight results with price drops that will emit updates when the data changes
     */
    fun getPriceDrops(
        searchId: String,
        priceThreshold: Double,
        previousPrice: Double
    ): Flow<List<CachedFlightResultEntity>>

    /**
     * Delete expired flight results from the local database.
     * @param currentTime The current timestamp
     */
    suspend fun deleteExpiredResults(currentTime: Long)

    /**
     * Delete all flight results for a specific search.
     * @param searchId The ID of the search
     */
    suspend fun deleteResultsBySearchId(searchId: String)

    /**
     * Synchronize flight results with the remote database.
     * @return The number of synchronized items
     */
    suspend fun syncWithRemote(): Int
} 