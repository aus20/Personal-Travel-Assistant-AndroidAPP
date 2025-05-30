package com.travelassistant.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.travelassistant.data.remote.api.AuthApiService // Bir önceki adımda oluşturduğumuz API servisi
// İleride eklenecek diğer API servisleri (FlightApiService vb.) buraya import edilecek
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.travelassistant.data.repository.AuthRepository
import com.travelassistant.data.repository.impl.AuthRepositoryImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import com.travelassistant.data.session.SessionManager
import android.content.Context
import com.travelassistant.data.remote.api.FlightApiService

// Bu modül, Retrofit ve OkHttpClient gibi ağ bileşenlerini sağlamak için kullanılır.
// Server ile iletişim kurmak için gerekli olan bileşenleri oluşturur.
@Module
@InstallIn(SingletonComponent::class) // Modülün uygulama seviyesinde (Singleton) olacağını belirtir
object NetworkModule {

    // TODO: Bu BASE_URL'i kendi Spring Boot sunucunuzun adresine göre güncelleyin!
    // Android emülatöründen geliştirme bilgisayarınızdaki localhost'a erişim için: "http://10.0.2.2:PORT_NUMARANIZ/"
    // Fiziksel bir cihazdan aynı ağdaki localhost'a erişim için: "http://BILGISAYARINIZIN_YEREL_IP_ADRESI:PORT_NUMARANIZ/"
    // Yayınlanmış bir sunucu için: "https://api.sizinadresiniz.com/"


    // aynı wifi'da çalıştığına emin olun
    private const val BASE_URL = "http://172.21.244.216:8080/"// Örnek localhost için emülatör adresi (Port 8080 varsayıldı)

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        // Geliştirme aşamasında tüm network loglarını görmek için BODY seviyesini kullanın.
        // Yayınlama aşamasında bunu Level.NONE veya Level.BASIC yapmayı düşünebilirsiniz.
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Network loglarını görmek için interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Bağlantı zaman aşımı
            .readTimeout(30, TimeUnit.SECONDS)    // Okuma zaman aşımı
            .writeTimeout(30, TimeUnit.SECONDS)   // Yazma zaman aşımı
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Sunucunun ana URL'si
            .client(okHttpClient) // Özelleştirilmiş OkHttpClient
            .addConverterFactory(GsonConverterFactory.create(gson)) // JSON işlemleri için Gson
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        // AuthApiService arayüzünün Retrofit implementasyonunu oluşturur
        return retrofit.create(AuthApiService::class.java)
    }

    // Gelecekte eklenecek diğer API servisleri için de benzer @Provides metodları burada yer alacak.
    // Örneğin:
     @Provides
     @Singleton
     fun provideFlightApiService(retrofit: Retrofit): FlightApiService {
         return retrofit.create(FlightApiService::class.java)
     }
    @Provides
    @Singleton
    fun provideUserRepository(authApiService: AuthApiService,sessionManager: SessionManager): AuthRepository {
        return AuthRepositoryImpl(authApiService,sessionManager)
    }
    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context,
        gson: Gson // provideGson() metodundan gelecek
    ): SessionManager {
        return SessionManager(context, gson)
    }
}