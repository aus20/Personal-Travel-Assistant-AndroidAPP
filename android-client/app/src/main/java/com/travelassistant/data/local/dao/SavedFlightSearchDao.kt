package com.travelassistant.data.local.dao

import androidx.room.*
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedFlightSearchDao {
    // CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SavedFlightSearchEntity)

    @Update
    suspend fun updateSearch(search: SavedFlightSearchEntity)

    @Delete
    suspend fun deleteSearch(search: SavedFlightSearchEntity)

    // Queries
    @Query("SELECT * FROM saved_flight_searches WHERE id = :searchId")
    suspend fun getSearchById(searchId: String): SavedFlightSearchEntity?

    @Query("SELECT * FROM saved_flight_searches WHERE userId = :userId")
    fun getSearchesByUserId(userId: String): Flow<List<SavedFlightSearchEntity>>

    @Query("SELECT * FROM saved_flight_searches WHERE lastSyncedAt < :timestamp")
    suspend fun getSearchesNeedingSync(timestamp: Long): List<SavedFlightSearchEntity>

    @Query("SELECT * FROM saved_flight_searches WHERE userId = :userId AND departureDate >= :currentTime")
    fun getActiveSearches(userId: String, currentTime: Long): Flow<List<SavedFlightSearchEntity>>

    // Complex Queries
    @Query("""
        SELECT * FROM saved_flight_searches 
        WHERE userId = :userId 
        AND departureDate >= :currentTime 
        AND (returnDate IS NULL OR returnDate >= :currentTime)
        ORDER BY departureDate ASC
    """)
    fun getUpcomingSearches(userId: String, currentTime: Long): Flow<List<SavedFlightSearchEntity>>

    // Transaction
    @Transaction
    suspend fun deleteExpiredSearches(currentTime: Long) {
        deleteSearchesByTimestamp(currentTime)
        deleteOrphanedResults()
    }

    @Query("DELETE FROM saved_flight_searches WHERE departureDate < :timestamp")
    suspend fun deleteSearchesByTimestamp(timestamp: Long)

    @Query("DELETE FROM cached_flight_results WHERE searchId NOT IN (SELECT id FROM saved_flight_searches)")
    suspend fun deleteOrphanedResults()
} 