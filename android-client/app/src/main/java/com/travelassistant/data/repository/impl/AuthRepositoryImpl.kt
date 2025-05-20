package com.travelassistant.data.repository.impl

import com.travelassistant.data.remote.api.AuthApiService
import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.remote.dto.response.UserResponse
import com.travelassistant.data.repository.AuthRepository
import com.travelassistant.data.remote.dto.UserLoginRequest   // <<-- YENİ IMPORT
import com.travelassistant.data.remote.dto.JwtLoginResponse // <<-- YENİ IMPORT
import com.travelassistant.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// AuthRepositoryImpl sınıfı, AuthRepository arayüzünü uygular
// Bu sınıf, kullanıcı kaydı gibi işlemleri gerçekleştirmek için gerekli olan API çağrılarını yapar
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepository {

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
    override suspend fun loginUser(userLoginRequest: UserLoginRequest): Result<JwtLoginResponse> {
        return try {
            val response = authApiService.loginUser(userLoginRequest)
            if (response.isSuccessful && response.body() != null) {
                // Başarılı giriş, JWT token'ı ve kullanıcı bilgileri geldi.
                // JWT token'ını burada saklama işlemi yapılabilir veya UseCase/ViewModel'e bırakılabilir.
                Result.Success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Login failed (HTTP ${response.code()})"
                Result.Error(errorMsg)
            }
        } catch (e: HttpException) {
            Result.Error("A HTTP error occurred: ${e.code()} - ${e.message()}", e)
        } catch (e: IOException) {
            Result.Error("Network error: Please check your internet connection.", e)
        } catch (e: Exception) {
            Result.Error("Unexpected error occurred: ${e.localizedMessage ?: "Unknown error"}", e)
        }
    }
}