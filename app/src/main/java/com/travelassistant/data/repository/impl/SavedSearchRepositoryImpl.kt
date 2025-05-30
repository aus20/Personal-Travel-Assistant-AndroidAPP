package com.travelassistant.data.repository.impl

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.travelassistant.data.remote.api.FlightApiService
import com.travelassistant.data.remote.dto.request.SaveFlightSearchRequest
import com.travelassistant.data.repository.SavedSearchRepository
import com.travelassistant.data.session.SessionManager
import com.travelassistant.data.remote.dto.response.SaveFlightSearchResponse
import com.travelassistant.data.remote.dto.response.DeleteSearchResponse
import com.travelassistant.data.remote.dto.response.GetUserSearchResponse
import com.travelassistant.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedSearchRepositoryImpl @Inject constructor(
    private val flightApiService: FlightApiService, // FlightApiService'i enjekte et
    private val sessionManager: SessionManager     // SessionManager'ı enjekte et
) : SavedSearchRepository {

    private val tag = "SavedSearchRepoImpl"

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun saveUserFlightSearch(
        saveFlightSearchRequest: SaveFlightSearchRequest
    ): Result<SaveFlightSearchResponse> {
        val token = sessionManager.getAuthToken()
        if (token.isNullOrBlank()) {
            Log.e(tag, "Kullanıcı giriş yapmamış veya token yok. Arama kaydedilemiyor.")
            return Result.Error("User not authenticated to save search.")
        }
        val bearerToken = "Bearer $token"
        Log.d(tag, "Uçuş araması kaydediliyor. Token: $bearerToken, Request: $saveFlightSearchRequest")

        // Network çağrıları IO thread'inde yapılmalı
        return withContext(Dispatchers.IO) {
            try {
                val response = flightApiService.saveFlightSearch( // FlightApiService'deki yeni metot
                    authToken = bearerToken,
                    saveFlightSearchRequest = saveFlightSearchRequest
                )

                if (response.isSuccessful && response.body() != null) {
                    Log.i(tag, "Uçuş araması başarıyla kaydedildi: ${response.body()}")
                    Result.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error during save"
                    Log.e(tag, "Uçuş araması kaydetme başarısız: ${response.code()} - $errorBody")
                    Result.Error("Save failed: ${response.code()} - $errorBody")
                }
            } catch (e: HttpException) {
                Log.e(tag, "HTTP Hatası (kaydetme): ${e.message}", e)
                Result.Error("A HTTP error occurred: ${e.message}", e)
            } catch (e: IOException) {
                Log.e(tag, "Network Hatası (kaydetme): İnternet bağlantısını kontrol edin.", e)
                Result.Error("Network error: Please check your internet connection.", e)
            } catch (e: Exception) {
                Log.e(tag, "Beklenmedik Hata (kaydetme): ${e.message}", e)
                Result.Error("Unexpected error occurred: ${e.message}", e)
            }
        }
    }
    // YENİ METOT IMPLEMENTASYONU
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun deleteSavedFlightSearch(
        searchId: Long // Veya ID tipiniz String ise String
    ): Result<DeleteSearchResponse> {
        val token = sessionManager.getAuthToken()
        if (token.isNullOrBlank()) {
            Log.e(tag, "Kullanıcı giriş yapmamış veya token yok. Arama silinemiyor.")
            return Result.Error("User not authenticated to delete search.")
        }
        val bearerToken = "Bearer $token"
        Log.d(tag, "Kaydedilmiş uçuş araması siliniyor. Token: $bearerToken, Search ID: $searchId")

        return withContext(Dispatchers.IO) {
            try {
                val response = flightApiService.deleteSavedFlightSearch( // FlightApiService'deki yeni metot
                    authToken = bearerToken,
                    searchId = searchId
                )

                if (response.isSuccessful && response.body() != null) {
                    Log.i(tag, "Kaydedilmiş arama başarıyla silindi: ${response.body()?.message}")
                    Result.Success(response.body()!!)
                } else if (response.isSuccessful && response.code() == 204) { // No Content durumu
                    Log.i(tag, "Kaydedilmiş arama başarıyla silindi (No Content).")
                    // Sunucu 204 ile boş body dönerse, özel bir DeleteSearchResponse oluşturabiliriz
                    // veya Result.Success(Unit) gibi bir şey dönebiliriz.
                    // Şimdilik DeleteSearchResponse beklediğimiz için, ve body null olabileceği için
                    // manuel bir response oluşturuyoruz ya da hata veriyoruz.
                    // En iyisi, eğer sunucu 204 dönebiliyorsa FlightApiService'deki dönüş tipini Response<Void>
                    // yapıp, burada Result.Success(Unit) dönmek.
                    // Şimdilik DTO beklediğimiz için ve body null olabileceği için:
                    Result.Success(DeleteSearchResponse(message = "Search deleted successfully (No Content)."))
                }
                else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error during delete"
                    Log.e(tag, "Kaydedilmiş arama silme başarısız: ${response.code()} - $errorBody")
                    Result.Error("Delete failed: ${response.code()} - $errorBody")
                }
            } catch (e: HttpException) {
                Log.e(tag, "HTTP Hatası (silme): ${e.message}", e)
                Result.Error("A HTTP error occurred: ${e.message}", e)
            } catch (e: IOException) {
                Log.e(tag, "Network Hatası (silme): İnternet bağlantısını kontrol edin.", e)
                Result.Error("Network error: Please check your internet connection.", e)
            } catch (e: Exception) {
                Log.e(tag, "Beklenmedik Hata (silme): ${e.message}", e)
                Result.Error("Unexpected error occurred: ${e.message}", e)
            }
        }
    }
    // YENİ METOT IMPLEMENTASYONU
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getUserSavedSearches(): Result<List<GetUserSearchResponse>> {
        val token = sessionManager.getAuthToken()
        if (token.isNullOrBlank()) {
            Log.e(tag, "Kullanıcı giriş yapmamış veya token yok. Kayıtlı aramalar getirilemiyor.")
            return Result.Error("User not authenticated to fetch saved searches.")
        }
        val bearerToken = "Bearer $token"
        Log.d(tag, "Kullanıcının kayıtlı uçuş aramaları getiriliyor. Token: $bearerToken")

        return withContext(Dispatchers.IO) {
            try {
                val response = flightApiService.getUserSavedSearches( // FlightApiService'deki yeni metot
                    authToken = bearerToken
                )

                if (response.isSuccessful && response.body() != null) {
                    Log.i(tag, "Kullanıcının kayıtlı aramaları başarıyla getirildi: ${response.body()?.size} adet")
                    Result.Success(response.body()!!)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error while fetching saved searches"
                    Log.e(tag, "Kayıtlı aramalar getirme başarısız: ${response.code()} - $errorBody")
                    Result.Error("Fetch saved searches failed: ${response.code()} - $errorBody")
                }
            } catch (e: HttpException) {
                Log.e(tag, "HTTP Hatası (kayıtlı aramalar getirme):${e.message}", e)
                Result.Error("A HTTP error occurred: ${e.message}", e)
            } catch (e: IOException) {
                Log.e(tag, "Network Hatası (kayıtlı aramalar getirme): İnternet bağlantısını kontrol edin.", e)
                Result.Error("Network error: Please check your internet connection.", e)
            } catch (e: Exception) {
                Log.e(tag, "Beklenmedik Hata (kayıtlı aramalar getirme): ${e.message}", e)
                Result.Error("Unexpected error occurred: ${e.message}", e)
            }
        }
    }

}