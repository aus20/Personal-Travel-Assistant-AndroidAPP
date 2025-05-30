package com.travelassistant.data.remote.api

import com.travelassistant.data.remote.dto.request.UserRegisterRequest // Oluşturduğunuz DTO
import com.travelassistant.data.remote.dto.response.UserResponse    // Oluşturduğunuz DTO
import com.travelassistant.data.remote.dto.response.JwtLoginResponse
import com.travelassistant.data.remote.dto.request.UserLoginRequest
import com.travelassistant.data.remote.dto.request.FcmTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


// Kullanıcı kaydı için API servisi
// Bu servis, kullanıcı kaydı için gerekli olan API çağrılarını içerir.
// Retrofit ile API çağrılarını gerçekleştirmek için bir arayüz oluşturuyoruz.
interface AuthApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body userRegisterRequest: UserRegisterRequest): Response<UserResponse>

    @POST("api/users/login") // <<-- YENİ METOT
    suspend fun loginUser(@Body userLoginRequest: UserLoginRequest): Response<JwtLoginResponse>

    @POST("api/users/fcm-token")
    suspend fun updateFcmToken(
        @Header("Authorization") authToken: String, // "Bearer <JWT_TOKEN>"
        @Body fcmTokenRequest: FcmTokenRequest
    ): Response<Void> // Sunucu bu endpoint için genellikle 200 OK ve boş body döner
}