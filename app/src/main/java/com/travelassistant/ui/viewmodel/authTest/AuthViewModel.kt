package com.travelassistant.ui.viewmodel.authTest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log // Loglama için
import com.google.firebase.messaging.FirebaseMessaging
import com.travelassistant.data.remote.dto.request.FcmTokenRequest
import com.travelassistant.data.remote.dto.request.UserLoginRequest
import com.travelassistant.data.session.SessionManager
import kotlinx.coroutines.tasks.await

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val testTag = "AuthViewModel_Test"

    fun testRegisterUser() {
        viewModelScope.launch {
            Log.d(testTag, "testRegisterUser başlatılıyor...")
            val uniqueEmail = "test_direct_${System.currentTimeMillis()}@example.com"
            val request = UserRegisterRequest(
                name = "Direct Test User",
                email = uniqueEmail,
                password = "password123"
            )
            val result = authRepository.registerUser(request) // Gerçek API çağrısı
            Log.d(testTag, "Kayıt sonucu: $result")
        }
    }
    private val testEmail = "test_direct_1748179994921@example.com" // <-- BURAYI GÜNCELLEMEN GEREKEBİLİR
    private val testPassword = "password123"

    fun testLoginUser() {
        viewModelScope.launch {
            Log.d(testTag, "testLoginUser başlatılıyor ($testEmail)...")
            val request = UserLoginRequest(
                email = testEmail, // <-- Kullanılacak email
                password = testPassword   // <-- Kullanılacak şifre
            )
            val result = authRepository.loginUser(request) // Gerçek API çağrısı
            Log.d(testTag, "Login sonucu: $result")
            testSendFcmToken()
        }
    }

    fun testSendFcmToken() {
        viewModelScope.launch {
            val authToken = sessionManager.getAuthToken()

            if (authToken.isNullOrBlank()) {
                Log.e(testTag, "JWT Auth Token bulunamadı. Önce login olunmalı.")
                return@launch
            }

            var currentFcmToken: String? = null
            try {
                // Firebase'den güncel FCM token'ını almayı dene
                currentFcmToken = FirebaseMessaging.getInstance().token.await()
                Log.i(testTag, "Güncel FCM Token Firebase'den alındı: $currentFcmToken")
            } catch (e: Exception) {
                Log.e(testTag, "Firebase'den FCM Token alınırken hata oluştu.", e)
                Log.e(testTag, "FCM Token alınamadığı için testSendFcmToken işlemi durduruldu.")
                return@launch
            }

            if (currentFcmToken.isNullOrBlank()) {
                Log.e(testTag, "Alınan FCM Token boş veya null. İşlem durduruldu.")
                return@launch
            }

            Log.d(testTag, "testSendFcmToken başlatılıyor...")
            Log.d(testTag, "Kullanılacak Auth Token: Bearer $authToken")
            Log.d(testTag, "Gönderilecek FCM Token: $currentFcmToken")

            val request = FcmTokenRequest(token = currentFcmToken)
            val result = authRepository.updateFcmToken(authToken, request)
            Log.d(testTag, "FCM Token gönderme sonucu: $result")
        }
    }
}