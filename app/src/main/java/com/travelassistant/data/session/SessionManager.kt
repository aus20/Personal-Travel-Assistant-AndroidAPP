package com.travelassistant.data.session

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.travelassistant.data.remote.dto.response.UserResponse // UserResponse DTO'muz
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Hilt ile uygulama genelinde tek bir instance olmasını sağlar
class SessionManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson // Kullanıcı bilgilerini JSON olarak saklamak/okumak için
) {

    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor

    var notificationPermissionGranted : Boolean
        get() = prefs.getBoolean(NOTIFICATION_PERMISSION_GRANTED, true)
        set(value) {
            prefs.edit().putBoolean(NOTIFICATION_PERMISSION_GRANTED, value).apply()
        }


    companion object {
        private const val PREFS_NAME = "travel_assistant_session"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_DETAILS = "user_details"


        private const val NOTIFICATION_PERMISSION_GRANTED = "NOTIFICATION_PERMISSION_GRANTED"

        // İsteğe bağlı: Kullanıcı ID'sini ayrıca saklamak istersen
        // private const val KEY_USER_ID = "user_id"

        var userDetail = MutableStateFlow<UserResponse?>(null)
    }

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    /**
     * JWT authentication token'ını kaydeder.
     */
    fun saveAuthToken(token: String) {
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply() // Asenkron olarak kaydeder
    }

    /**
     * Kayıtlı JWT authentication token'ını alır.
     * @return Kayıtlı token veya token yoksa null.
     */
    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Kullanıcı bilgilerini JSON string olarak kaydeder.
     */
    fun saveUserDetails(user: UserResponse?) {
        if (user == null) {
            editor.remove(KEY_USER_DETAILS)
        } else {
            val userJson = gson.toJson(user)
            editor.putString(KEY_USER_DETAILS, userJson)
        }
        editor.apply()
        userDetail.value = user
    }

    /**
     * Kayıtlı kullanıcı bilgilerini UserResponse objesi olarak alır.
     * @return Kayıtlı UserResponse objesi veya bilgi yoksa null.
     */
    fun getUserDetails(): UserResponse? {
        val userJson = prefs.getString(KEY_USER_DETAILS, null)
        return userJson?.let {
            try {
                gson.fromJson(it, UserResponse::class.java)
            } catch (e: Exception) {
                // JSON parse hatası durumunda null dön veya logla
                null
            }
        }
    }

    /**
     * Kullanıcının giriş yapıp yapmadığını kontrol eder (token var mı?).
     * @return Token varsa true, yoksa false.
     */
    fun isUserLoggedIn(): Boolean {
        return !getAuthToken().isNullOrBlank()
    }

    /**
     * Mevcut oturumu (token ve kullanıcı bilgileri) temizler.
     * Genellikle kullanıcı çıkış yaptığında çağrılır.
     */
    fun clearSession() {
        editor.remove(KEY_AUTH_TOKEN)
        editor.remove(KEY_USER_DETAILS)
        // editor.remove(KEY_USER_ID) // Eğer kullanıcı ID'sini ayrıca saklıyorsan
        editor.apply()

        userDetail.value = null
    }
}