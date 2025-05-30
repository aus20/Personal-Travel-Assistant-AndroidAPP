package com.travelassistant.data.repository.impl

import android.util.Log
import com.travelassistant.data.local.dao.SavedFlightSearchDao
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.remote.api.FlightApiService
import com.travelassistant.data.remote.dto.request.FlightSearchRequest
import com.travelassistant.data.remote.dto.response.FlightSearchResponse
import com.travelassistant.data.session.SessionManager
import com.travelassistant.util.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
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
    private val networkStateManager: NetworkStateManager,
    private val flightApiService: FlightApiService, // <-- YENİ BAĞIMLILIK
    private val sessionManager: SessionManager    // <-- YENİ BAĞIMLILIK
) : FlightSearchRepository {

    private val tag = "FlightSearchRepoImpl"

    // <-- YENİ METOT IMPLEMENTASYONU -->
    override suspend fun searchFlights(request: FlightSearchRequest): Result<FlightSearchResponse> {
        // Token'ı al
        val token = sessionManager.getAuthToken()
        if (token.isNullOrBlank()) {
            Log.e(tag, "Kullanıcı giriş yapmamış. Uçuş araması yapılamıyor.")
            return com.travelassistant.util.Result.Error("User not logged in. Cannot perform search.")
        }
        val bearerToken = "Bearer $token"
        Log.d(tag, "Uçuş araması yapılıyor. Token: $bearerToken, Request: $request")

        // Network bağlantısını kontrol et (Opsiyonel, ViewModel'de de yapılabilir)
        if (!networkStateManager.isOnline.value) {
            Log.w(tag, "İnternet bağlantısı yok. Arama yapılamıyor.")
            return com.travelassistant.util.Result.Error("Network error: Please check your internet connection.")
        }

        return try {
            // API çağrısını yap
            val response = flightApiService.searchFlights(bearerToken, request)

            if (response.isSuccessful && response.body() != null) {
                Log.i(tag, "Uçuş araması başarılı. Yanıt: ${response.body()}")
                com.travelassistant.util.Result.Success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Log.e(tag, "Uçuş araması başarısız: ${response.code()} - $errorBody")
                com.travelassistant.util.Result.Error("Search failed: ${response.code()} - $errorBody")
            }
        } catch (e: HttpException) {
            Log.e(tag, "HTTP Hatası: ${e.message()}", e)
            com.travelassistant.util.Result.Error("A HTTP error occurred: ${e.message()}", e)
        } catch (e: IOException) {
            Log.e(tag, "Network Hatası: İnternet bağlantısını kontrol edin.", e)
            com.travelassistant.util.Result.Error("Network error: Please check your internet connection.", e)
        } catch (e: Exception) {
            Log.e(tag, "Beklenmedik Hata: ${e.message}", e)
            Result.Error("Unexpected error occurred: ${e.message}", e)
        }
    }

    override suspend fun insertSearch(search: SavedFlightSearchEntity): String {
        val searchWithId = search.let {
            if (search.id.isEmpty()) {
                search.copy(id = "${it.origin}-${it.destination}-${it.departureDate}")
            } else {
                search
            }
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

    override fun getActiveSearches(userId: Long?, currentTime: Long): Flow<List<SavedFlightSearchEntity>> {
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