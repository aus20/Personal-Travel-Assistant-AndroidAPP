package com.travelassistant.data.local.dao

import androidx.room.*
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CachedFlightResultDao {
    // CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlightResult(result: CachedFlightResultEntity)

    @Update
    suspend fun updateFlightResult(result: CachedFlightResultEntity)

    @Delete
    suspend fun deleteFlightResult(result: CachedFlightResultEntity)

    // Queries
    @Query("SELECT * FROM cached_flight_results WHERE id = :flightId")
    suspend fun getFlightResultById(flightId: String): CachedFlightResultEntity?

    @Query("SELECT * FROM cached_flight_results WHERE searchId = :searchId")
    fun getFlightResultsBySearchId(searchId: String): Flow<List<CachedFlightResultEntity>>

    @Query("SELECT * FROM cached_flight_results WHERE expiresAt < :currentTime")
    suspend fun getExpiredResults(currentTime: Long): List<CachedFlightResultEntity>

    // Complex Queries
    @Query("""
        SELECT * FROM cached_flight_results 
        WHERE searchId = :searchId 
        AND price <= :maxPrice 
        AND stops <= :maxStops
        ORDER BY price ASC
    """)
    fun getMatchingFlights(
        searchId: String,
        maxPrice: Double,
        maxStops: Int
    ): Flow<List<CachedFlightResultEntity>>

    @Query("""
        SELECT * FROM cached_flight_results 
        WHERE searchId = :searchId 
        AND departureTime >= :startTime 
        AND departureTime <= :endTime
        ORDER BY departureTime ASC
    """)
    fun getFlightsInTimeRange(
        searchId: String,
        startTime: Long,
        endTime: Long
    ): Flow<List<CachedFlightResultEntity>>

    // Price Tracking
    @Query("""
        SELECT * FROM cached_flight_results 
        WHERE searchId = :searchId 
        AND price <= :priceThreshold 
        AND price > :previousPrice
        ORDER BY price ASC
    """)
    fun getPriceDrops(
        searchId: String,
        priceThreshold: Double,
        previousPrice: Double
    ): Flow<List<CachedFlightResultEntity>>

    // Cleanup
    @Query("DELETE FROM cached_flight_results WHERE expiresAt < :currentTime")
    suspend fun deleteExpiredResults(currentTime: Long)

    @Query("DELETE FROM cached_flight_results WHERE searchId = :searchId")
    suspend fun deleteResultsBySearchId(searchId: String)
} 