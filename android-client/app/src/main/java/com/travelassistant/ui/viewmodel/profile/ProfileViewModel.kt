package com.travelassistant.ui.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import com.travelassistant.data.local.entity.AppPreferencesEntity // Bu önemli
import com.travelassistant.ui.viewmodel.profile.UserProfile // Zaten olmalı
import com.travelassistant.ui.viewmodel.profile.UserPreferences // Zaten olmalı
import com.travelassistant.ui.viewmodel.profile.NotificationSettings
import com.travelassistant.ui.viewmodel.profile.SearchPreferences
import com.travelassistant.ui.viewmodel.profile.DisplayPreferences
import com.travelassistant.ui.viewmodel.profile.Theme // Enum için
import com.travelassistant.ui.viewmodel.profile.SortOption // Enum için
import com.travelassistant.ui.viewmodel.profile.SyncFrequency // Enum için
// import com.travelassistant.data.session.SessionManager // Opsiyonel: Kullanıcı ID'si için



@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val networkStateManager: NetworkStateManager,
    // private val sessionManager: SessionManager // Opsiyonel: Aktif kullanıcı ID'sini almak için
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    // Geçici olarak bir kullanıcı ID'si, idealde SessionManager'dan alınmalı
    private val currentUserId = "sampleUserId" // TODO: Bu ID'yi dinamik olarak alın (örn: SessionManager)

    init {
        viewModelScope.launch {
            networkStateManager.isOnline.collect { isOnline ->
                _isOnline.value = isOnline
            }
        }
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            
            try {
                var appPrefs = preferencesRepository.getPreferencesByUserId(currentUserId)
                if (appPrefs == null) {
                    // Varsayılan tercihleri oluştur
                    preferencesRepository.createDefaultPreferences(
                        currentUserId,
                        System.currentTimeMillis()
                    )
                    appPrefs = preferencesRepository.getPreferencesByUserId(currentUserId)
                    if (appPrefs == null) {
                        _profileState.value =
                            ProfileState.Error("Kullanıcı tercihleri oluşturulamadı veya bulunamadı.")
                        return@launch
                    }
                }
                // AppPreferencesEntity'den UserProfile'a mapleme
                val userProfile = appPrefs.toUserProfile(currentUserId, "Kullanıcı Adı", "kullanici@example.com") // TODO: Ad ve email nereden gelecek?
                _profileState.value = ProfileState.Success(userProfile = userProfile)
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }
/*
    fun updateProfileName(name: String) {
        viewModelScope.launch {
            val currentState = _profileState.value
            if (currentState is ProfileState.Success) {
                try {
                    val updatedProfile = currentState.userProfile.copy(
                        name = name, // Sadece isim güncelleniyor UserProfile'da
                        lastUpdated = Date()
                    )
                    // Güncellenmiş UserProfile'ı AppPreferencesEntity'ye mapleyip kaydet
                    updatePreferencesInRepository(updatedProfile)
                } catch (e: Exception) {
                    updateErrorState("İsim güncellenirken hata: ${e.message}")
                }
            }
        }
    }
 */

 /*
    fun updatePhoneNumber(phoneNumber: String?) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedProfile = currentState.userProfile.copy(
                    phoneNumber = phoneNumber,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update phone number: ${e.message}")
            }
        }
    }

 */
    /*

    fun updateNotificationSettings(settings: NotificationSettings) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    notificationSettings = settings
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update notification settings: ${e.message}")
            }
        }
    }

     */
    /*
    fun updateSearchPreferences(preferences: SearchPreferences) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    searchPreferences = preferences
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update search preferences: ${e.message}")
            }
        }
    }

    fun updateDisplayPreferences(preferences: DisplayPreferences) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    displayPreferences = preferences
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update display preferences: ${e.message}")
            }
        }
    }

    fun updateSyncPreferences(preferences: SyncPreferences) {
        viewModelScope.launch {
            try {
                val currentState = _profileState.value as? ProfileState.Success
                if (currentState == null) return@launch

                val updatedPreferences = currentState.userProfile.preferences.copy(
                    syncPreferences = preferences
                )
                val updatedProfile = currentState.userProfile.copy(
                    preferences = updatedPreferences,
                    lastUpdated = Date()
                )
                preferencesRepository.updateUserProfile(updatedProfile)
                _profileState.value = currentState.copy(userProfile = updatedProfile)
            } catch (e: Exception) {
                updateErrorState("Failed to update sync preferences: ${e.message}")
            }
        }
    }

     */

    private fun updateErrorState(errorMessage: String) {
        _profileState.update { currentState ->
            when (currentState) {
                is ProfileState.Success -> currentState.copy(
                    error = errorMessage,
                    isLoading = false
                )
                else -> ProfileState.Error(errorMessage)
            }
        }
    }
    // --- Mapper Fonksiyonları (Bu fonksiyonları ProfileViewModel.kt dışına, ayrı bir mapper dosyasına taşımak daha iyi olabilir) ---

    fun AppPreferencesEntity.toUserProfile(userId: String, name: String, email: String): UserProfile {
        // AppPreferencesEntity'den UserProfile'a mapleme
        // UserProfile'daki bazı alanlar (name, email, phoneNumber) AppPreferencesEntity'de doğrudan yok.
        // Bunların nasıl yönetileceğine karar vermelisiniz.
        // Belki name ve email SessionManager'dan veya başka bir kaynaktan gelmeli.
        // Şimdilik örnek olarak parametre ile alıyoruz.
        return UserProfile(
            id = userId, // AppPreferencesEntity'deki userId ile aynı olmalı
            name = name, // Bu alan AppPreferencesEntity'de yok, nasıl doldurulacak?
            email = email, // Bu alan AppPreferencesEntity'de yok
            phoneNumber = null, // Bu alan AppPreferencesEntity'de yok, nasıl doldurulacak?
            preferences = UserPreferences(
                notificationSettings = NotificationSettings( // AppPreferencesEntity'deki alanlarla eşleştir
                    priceAlerts = this.notificationEnabled, // Örnek, tam eşleşmeyebilir
                    emailNotifications = this.notificationEnabled, // Örnek
                    pushNotifications = this.notificationEnabled // Örnek
                    // Diğer NotificationSettings alanları için varsayılan veya AppPreferencesEntity'den gelen değerler
                ),
                searchPreferences = SearchPreferences( // AppPreferencesEntity'deki alanlarla eşleştir
                    priceRange = 0f..this.priceAlertThreshold.toFloat() // Örnek
                    // Diğer SearchPreferences alanları
                ),
                displayPreferences = DisplayPreferences( // AppPreferencesEntity'deki alanlarla eşleştir
                    currency = this.preferredCurrency,
                    language = this.language
                    // Diğer DisplayPreferences alanları
                ),
                syncPreferences = SyncPreferences( // AppPreferencesEntity'deki alanlarla eşleştir
                    // SyncPreferences için AppPreferencesEntity'de karşılık gelen alanlar yok gibi
                )
            ),
            createdAt = Date(this.updatedAt ?: System.currentTimeMillis()), // AppPrefs'teki updatedAt'i kullanabiliriz
            lastUpdated = Date(this.updatedAt ?: System.currentTimeMillis())
        )
    }

    fun UserProfile.toAppPreferencesEntity(originalLastSyncTimestamp: Long): AppPreferencesEntity {
        // UserProfile'dan AppPreferencesEntity'ye mapleme
        return AppPreferencesEntity(
            userId = this.id,
            notificationEnabled = this.preferences.notificationSettings.pushNotifications, // Örnek
            priceAlertThreshold = this.preferences.searchPreferences.priceRange.endInclusive.toDouble(), // Örnek
            preferredCurrency = this.preferences.displayPreferences.currency,
            language = this.preferences.displayPreferences.language,
            lastSyncTimestamp = originalLastSyncTimestamp, // Veya System.currentTimeMillis()
            updatedAt = this.lastUpdated.time
            // AppPreferencesEntity'deki diğer alanlar UserProfile.preferences'dan maplenmeli
        )
    }
} 