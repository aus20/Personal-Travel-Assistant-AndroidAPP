package com.travelassistant.data.remote.api

import com.travelassistant.data.remote.dto.request.FlightSearchRequest
import com.travelassistant.data.remote.dto.request.SaveFlightSearchRequest
import com.travelassistant.data.remote.dto.response.FlightSearchResponse
import com.travelassistant.data.remote.dto.response.SaveFlightSearchResponse
import com.travelassistant.data.remote.dto.response.DeleteSearchResponse
import com.travelassistant.data.remote.dto.response.GetUserSearchResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.DELETE // YENİ IMPORT
import retrofit2.http.GET
import retrofit2.http.Path     // YENİ IMPORT

interface FlightApiService {

    @POST("api/flights/search/advanced")
    suspend fun searchFlights(
        @Header("Authorization") authToken: String, // "Bearer <JWT_TOKEN>"
        @Body flightSearchRequest: FlightSearchRequest
    ): Response<FlightSearchResponse> // Başarılı yanıt için FlightSearchResponse bekliyoruz

    // YENİ METOT: Uçuş Aramasını Kaydetme
    @POST("api/user-searches")
    suspend fun saveFlightSearch(
        @Header("Authorization") authToken: String, // Bu endpoint auth gerektiriyorsa
        @Body saveFlightSearchRequest: SaveFlightSearchRequest
    ): Response<SaveFlightSearchResponse>

    // YENİ METOT: Kaydedilmiş Uçuş Aramasını Silme
    @DELETE("api/user-searches/{searchId}") // Endpoint'i kendi sunucunuza göre güncelleyin
    suspend fun deleteSavedFlightSearch(
        @Header("Authorization") authToken: String,
        @Path("searchId") searchId: Long // Veya String, ID tipinize göre
    ): Response<DeleteSearchResponse>

    @GET("api/user-searches")
    suspend fun getUserSavedSearches(
        @Header("Authorization") authToken: String
    ): Response<List<GetUserSearchResponse>> // Dönen tip, bir liste olacak

}

