package com.travelassistant.data.repository

import com.travelassistant.data.remote.dto.request.SaveFlightSearchRequest
import com.travelassistant.data.remote.dto.response.DeleteSearchResponse
import com.travelassistant.data.remote.dto.response.GetUserSearchResponse
import com.travelassistant.data.remote.dto.response.SaveFlightSearchResponse
import com.travelassistant.util.Result

interface SavedSearchRepository {
    /**
     * Kullanıcının uçuş aramasını sunucuya kaydeder.
     */
    suspend fun saveUserFlightSearch(
        saveFlightSearchRequest: SaveFlightSearchRequest
    ): Result<SaveFlightSearchResponse>

    suspend fun deleteSavedFlightSearch(
        searchId: Long // Veya ID tipiniz String ise String
    ): Result<DeleteSearchResponse> // Dönüş tipini yeni DTO ile güncelle

    /**
     * Kullanıcının sunucuda kayıtlı tüm uçuş aramalarını getirir.
     */
    suspend fun getUserSavedSearches(): Result<List<GetUserSearchResponse>> // YENİ METOT
}

