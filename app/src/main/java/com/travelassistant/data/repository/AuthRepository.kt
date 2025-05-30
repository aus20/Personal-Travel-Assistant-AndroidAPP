package com.travelassistant.data.repository

import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.remote.dto.response.UserResponse
import com.travelassistant.data.remote.dto.request.UserLoginRequest
import com.travelassistant.data.remote.dto.response.JwtLoginResponse
import com.travelassistant.util.Result // Kendi Result sarmalayıcı sınıfımız (aşağıda oluşturulacak)
import com.travelassistant.data.remote.dto.request.FcmTokenRequest
interface AuthRepository {
    suspend fun registerUser(userRegisterRequest: UserRegisterRequest): Result<UserResponse>
    suspend fun loginUser(userLoginRequest: UserLoginRequest): Result<JwtLoginResponse>
    suspend fun updateFcmToken(authToken: String, fcmTokenRequest: FcmTokenRequest): Result<Unit>


    // Login, FCM token gönderme vb. metodlar da buraya eklenecek
}