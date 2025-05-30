package com.travelassistant.data.repository

import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.remote.dto.request.FlightSearchRequest
import com.travelassistant.data.remote.dto.response.FlightSearchResponse
import com.travelassistant.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for flight search operations.
 * This repository handles both local storage and remote synchronization of flight searches.
 */
interface FlightSearchRepository {



    suspend fun searchFlights(request: FlightSearchRequest): Result<FlightSearchResponse> // <-- YENİ METOT
    /**
     * Insert a new flight search into the local database.
     * @param search The flight search to insert
     * @return The ID of the inserted search
     */
    suspend fun insertSearch(search: SavedFlightSearchEntity): String

    /**
     * Update an existing flight search in the local database.
     * @param search The flight search to update
     */
    suspend fun updateSearch(search: SavedFlightSearchEntity)

    /**
     * Delete a flight search from the local database.
     * @param search The flight search to delete
     */
    suspend fun deleteSearch(search: SavedFlightSearchEntity)

    /**
     * Get a flight search by its ID.
     * @param searchId The ID of the flight search to retrieve
     * @return The flight search, or null if not found
     */
    suspend fun getSearchById(searchId: String): SavedFlightSearchEntity?

    /**
     * Get all flight searches for a specific user.
     * @param userId The ID of the user
     * @return A Flow of flight searches that will emit updates when the data changes
     */
    fun getSearchesByUserId(userId: String): Flow<List<SavedFlightSearchEntity>>

    /**
     * Get all active flight searches for a specific user.
     * @param userId The ID of the user
     * @param currentTime The current timestamp
     * @return A Flow of active flight searches that will emit updates when the data changes
     */
    fun getActiveSearches(userId: Long?, currentTime: Long): Flow<List<SavedFlightSearchEntity>>

    /**
     * Get all upcoming flight searches for a specific user.
     * @param userId The ID of the user
     * @param currentTime The current timestamp
     * @return A Flow of upcoming flight searches that will emit updates when the data changes
     */
    fun getUpcomingSearches(userId: String, currentTime: Long): Flow<List<SavedFlightSearchEntity>>

    /**
     * Delete expired flight searches from the local database.
     * @param currentTime The current timestamp
     */
    suspend fun deleteExpiredSearches(currentTime: Long)

    /**
     * Synchronize flight searches with the remote database.
     * @return The number of synchronized items
     */
    suspend fun syncWithRemote(): Int

    /**
     * Get flight searches that need to be synchronized with the remote database.
     * @param timestamp The timestamp to compare against
     * @return A list of flight searches that need to be synchronized
     */
    suspend fun getSearchesNeedingSync(timestamp: Long): List<SavedFlightSearchEntity>
} 