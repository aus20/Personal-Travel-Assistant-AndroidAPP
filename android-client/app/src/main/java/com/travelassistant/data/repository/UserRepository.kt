package com.travelassistant.data.repository

import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.remote.dto.response.UserResponse
import com.travelassistant.util.Result // Kendi Result sarmalayıcı sınıfımız (aşağıda oluşturulacak)

interface UserRepository {
    suspend fun registerUser(userRegisterRequest: UserRegisterRequest): Result<UserResponse>

    // Login, FCM token gönderme vb. metodlar da buraya eklenecek
}