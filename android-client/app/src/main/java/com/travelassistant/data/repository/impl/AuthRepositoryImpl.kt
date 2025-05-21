package com.travelassistant.data.repository.impl

import com.travelassistant.data.remote.api.AuthApiService
import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.remote.dto.response.UserResponse
import com.travelassistant.data.repository.AuthRepository
import com.travelassistant.data.remote.dto.request.UserLoginRequest
import com.travelassistant.data.remote.dto.request.FcmTokenRequest // <<-- YENİ IMPORT
import com.travelassistant.data.remote.dto.response.JwtLoginResponse // <<-- YENİ IMPORT
import com.travelassistant.data.session.SessionManager
import com.travelassistant.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import android.util.Log


// AuthRepositoryImpl sınıfı, AuthRepository arayüzünü uygular
// Bu sınıf, kullanıcı kaydı gibi işlemleri gerçekleştirmek için gerekli olan API çağrılarını yapar
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    private val tag = "AuthRepositoryImpl"

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
                val jwtResponse = response.body()!!
                // BAŞARILI GİRİŞ SONRASI TOKEN VE KULLANICI BİLGİLERİNİ KAYDET
                sessionManager.saveAuthToken(jwtResponse.token)
                sessionManager.saveUserDetails(jwtResponse.user)
                Log.d(tag, "Giriş başarılı. Token ve kullanıcı bilgileri kaydedildi.")
                Result.Success(jwtResponse)
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
    override suspend fun updateFcmToken(authToken: String, fcmTokenRequest: FcmTokenRequest): Result<Unit> {
        return withContext(Dispatchers.IO) { // Arka plan thread'inde çalıştır
            // BEARER KISIMLARI DEĞİŞEBİLİR
            try {
                val bearerAuthToken = if (authToken.startsWith("Bearer ")) authToken else "Bearer $authToken"
                Log.d("AuthRepositoryImpl", "Updating FCM token with auth: $bearerAuthToken") // Loglama

                // AuthApiService içindeki metod adının updateUserFcmToken olduğunu varsayıyoruz
                val response = authApiService.updateFcmToken(bearerAuthToken, fcmTokenRequest)

                if (response.isSuccessful) {
                    Log.i("AuthRepositoryImpl", "FCM token successfully updated on server.")
                    Result.Success(Unit)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "FCM token update unsuccessful. (HTTP ${response.code()})"
                    Log.e("AuthRepositoryImpl", "FCM token update failed: $errorMsg")
                    Result.Error(errorMsg)
                }
            } catch (e: HttpException) {
                Result.Error("HTTP error : ${e.code()} - ${e.message()}", e)
            } catch (e: IOException) {
                Result.Error(
                    "Network connection cannot be established.Please check you internet connection",
                    e
                )
            } catch (e: Exception) {
                Result.Error("Unknown error: ${e.localizedMessage ?: "Unknown error"}", e)
            }
        }
    }
}