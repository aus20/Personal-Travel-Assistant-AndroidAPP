package com.travelassistant.data.remote.api

import com.travelassistant.data.remote.dto.request.UserRegisterRequest // Oluşturduğunuz DTO
import com.travelassistant.data.remote.dto.response.UserResponse    // Oluşturduğunuz DTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
// Kullanıcı kaydı için API servisi
// Bu servis, kullanıcı kaydı için gerekli olan API çağrılarını içerir.
// Retrofit ile API çağrılarını gerçekleştirmek için bir arayüz oluşturuyoruz.
interface AuthApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body userRegisterRequest: UserRegisterRequest): Response<UserResponse>
}