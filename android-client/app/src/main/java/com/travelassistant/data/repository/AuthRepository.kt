package com.travelassistant.data.repository

import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.remote.dto.response.UserResponse
import com.travelassistant.util.Result // Kendi Result sarmalayıcı sınıfımız (aşağıda oluşturulacak)

interface AuthRepository {
    suspend fun registerUser(userRegisterRequest: UserRegisterRequest): Result<UserResponse>
    suspend fun loginUser(userLoginRequest: UserLoginRequest): Result<JwtLoginResponse>

    // Login, FCM token gönderme vb. metodlar da buraya eklenecek
}