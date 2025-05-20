package com.travelassistant.data.repository.impl

import com.travelassistant.data.remote.api.AuthApiService
import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.remote.dto.response.UserResponse
import com.travelassistant.data.repository.UserRepository
import com.travelassistant.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : UserRepository {

    override suspend fun registerUser(userRegisterRequest: UserRegisterRequest): Result<UserResponse> {
        return withContext(Dispatchers.IO) { // Network çağrıları IO thread'inde yapılmalı
            try {
                val response = authApiService.registerUser(userRegisterRequest)
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    // Sunucudan gelen hata mesajını ayrıştırmaya çalışabiliriz (opsiyonel)
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    // TODO: errorBody'yi daha anlamlı bir mesaja dönüştür
                    Result.Error("Register failed: ${response.code()} - $errorBody")
                }
            } catch (e: HttpException) { // HTTP hataları (4xx, 5xx)
                Result.Error("A HTTP error occurred: ${e.message()}", e)
            } catch (e: IOException) { // Network bağlantı sorunları
                Result.Error("Network error: Please check your internet connection.", e)
            } catch (e: Exception) { // Diğer beklenmedik hatalar
                Result.Error("Unexpected error occurred: ${e.message}", e)
            }
        }
    }
}