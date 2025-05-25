package com.travelassistant.data.repository/*
package com.travelassistant.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.travelassistant.data.remote.dto.request.UserRegisterRequest
import com.travelassistant.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class) // runTest için
@MediumTest // Bu testin network isteği yapacağını belirtir (SmallTest, MediumTest, LargeTest)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest // Hilt'in bu test sınıfı için bağımlılıkları enjekte etmesini sağlar
class AuthRepositoryIntegrationTest {

    // Hilt kurallarını ve enjeksiyonunu yönetir
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // AuthRepository'yi Hilt aracılığıyla enjekte et
    // NetworkModule'de provideUserRepository metodunuzun doğru tanımlandığını varsayıyoruz
    @Inject
    lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        hiltRule.inject() // Enjeksiyonları gerçekleştir
        // Test öncesi yapılacak ek ayarlar (varsa)
    }

    @Test
    fun registerUser_withValidData_shouldReturnSuccessAndUserData() = runTest {
        // BENZERSİZ BİR E-POSTA KULLANMAYA DİKKAT EDİN, HER TEST ÇALIŞTIĞINDA AYNI E-POSTA İLE KAYIT YAPAMAZSINIZ
        val uniqueEmail = "testuser_integration_${System.currentTimeMillis()}@example.com"
        val userRegisterRequest = UserRegisterRequest(
            name = "Integration Test User",
            email = uniqueEmail,
            password = "password123"
        )

        // authRepository üzerinden API çağrısını yap
        val result = authRepository.registerUser(userRegisterRequest)

        // Sonucu kontrol et
        assertTrue("Kayıt sonucu Result.Success olmalıydı, gelen: $result", result is Result.Success)
        val userResponse = (result as Result.Success).data
        assertNotNull("UserResponse null olmamalıydı", userResponse)
        assertEquals("İsim eşleşmiyor", userRegisterRequest.name, userResponse.name)
        assertEquals("E-posta eşleşmiyor", userRegisterRequest.email, userResponse.email)
        assertFalse("Kullanıcı ID'si boş olmamalıydı", userResponse.id.isNullOrBlank())

        // Logcat'e de bakarak isteğin gidip gitmediğini ve sunucudan dönen yanıtı görebilirsin.
        // NetworkModule'deki HttpLoggingInterceptor logları yazdıracaktır.
        System.out.println("Kayıt Testi Başarılı: $userResponse")
    }

    @Test
    fun registerUser_withExistingEmail_shouldReturnError() = runTest {
        // ÖNCE GEÇERLİ BİR KULLANICI KAYDET (VEYA SUNUCUDA ZATEN MEVCUT OLAN BİR E-POSTA KULLAN)
        val existingEmail = "existing${System.currentTimeMillis()}@example.com" // Her çalıştırmada farklı olması için
        val initialRequest = UserRegisterRequest("Initial User", existingEmail, "password123")
        val initialResult = authRepository.registerUser(initialRequest)
        assertTrue("İlk kayıt başarılı olmalıydı", initialResult is Result.Success)


        // ŞİMDİ AYNI E-POSTA İLE TEKRAR KAYIT OLMAYA ÇALIŞ
        val duplicateRequest = UserRegisterRequest("Duplicate User", existingEmail, "password456")
        val result = authRepository.registerUser(duplicateRequest)

        // Sonucu kontrol et (Sunucunuzun bu durumda nasıl bir hata döndüğüne bağlı olarak)
        // Genellikle 400 (Bad Request) veya 409 (Conflict) gibi bir HTTP hatası ve
        // "E-posta zaten kullanılıyor" gibi bir mesaj beklenir.
        assertTrue("Aynı e-posta ile kayıt sonucu Result.Error olmalıydı, gelen: $result", result is Result.Error)
        val errorMessage = (result as Result.Error).message
        assertNotNull("Hata mesajı null olmamalıydı", errorMessage)
        // Sunucunuzun döndüğü spesifik hata mesajını veya kodunu burada kontrol edebilirsiniz.
        // Örneğin: assertTrue(errorMessage.contains("Email already exists") || errorMessage.contains("409"))
        System.out.println("Mevcut E-posta Kayıt Testi Başarılı (Hata Bekleniyordu): $errorMessage")
    }


    // Benzer şekilde loginUser ve updateUserFcmToken için de entegrasyon testleri yazılabilir.
    // loginUser testi için, önce bir kullanıcı kaydetmeniz veya veritabanında kayıtlı bir kullanıcı kullanmanız gerekir.
    // updateUserFcmToken testi için, önce giriş yapıp geçerli bir JWT token almanız gerekir.
}
*/