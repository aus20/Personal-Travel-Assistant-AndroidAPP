package com.travelassistant.data.services.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.travelassistant.MainActivity // Ana aktiviteniz
import com.travelassistant.R // Kaynaklar için R sınıfı
import com.travelassistant.data.repository.NotificationRepository // Bildirimleri kaydetmek için (opsiyonel)
import com.travelassistant.data.repository.AuthRepository // Token'ı sunucuya göndermek için (oluşturulacak)
import com.travelassistant.data.remote.dto.request.FcmTokenRequest
import com.travelassistant.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var authRepository: AuthRepository // Hilt ile enjekte edilecek

    private val TAG = "MyFirebaseMsgService"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * FCM registration token her değiştiğinde çağrılır.
     * Bu token, uygulamanın bu cihaza bildirim alabilmesi için gereklidir.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    /**
     * Yeni FCM token'ını sunucuya gönderir.
     */
    private fun sendRegistrationToServer(newFcmToken: String?) {
        newFcmToken?.let { fcmToken ->
            coroutineScope.launch {
                val jwtAuthToken = retrieveCurrentAuthToken() // Bu metodu implemente etmeliyiz

                if (jwtAuthToken.isNullOrBlank()) {
                    Log.w(TAG, "Kullanıcı giriş yapmamış veya JWT token bulunamadı. FCM token gönderilemiyor.")
                    return@launch
                }

                // FcmTokenRequest objesini burada oluşturuyoruz
                val requestDto = FcmTokenRequest(token = fcmToken)

                // AuthRepository (veya UserRepository) üzerinden token güncelleme
                when (val result = authRepository.updateFcmToken(jwtAuthToken, requestDto)) {
                    is Result.Success -> {
                        Log.i(TAG, "FCM Token sunucuya başarıyla gönderildi/güncellendi.")
                    }
                    is Result.Error -> {
                        Log.e(TAG, "FCM Token sunucuya gönderilirken hata oluştu: ${result.message}")
                        // Gerekirse tekrar deneme mekanizması (WorkManager vb.)
                    }
                    else -> { /* Result.Loading durumu ele alınabilir */ }
                }
            }
        }
    }
    // Placeholder: Gerçek JWT token'ı alma mantığı buraya gelmeli
    private suspend fun retrieveCurrentAuthToken(): String? {
        // TODO: Bu metod, giriş yapmış kullanıcının JWT token'ını güvenli bir yerden
        // (örn: şifreli SharedPreferences, DataStore, AuthManager) okumalıdır.
        // Kullanıcı Girişi (Login) use case'i tamamlandığında burası doldurulacak.
        Log.w(TAG, "retrieveCurrentAuthToken() - Placeholder: Gerçek JWT token alma implementasyonu gerekli.")
        // ŞİMDİLİK TEST İÇİN ELLE BİR TOKEN GİREBİLİR YA DA NULL DÖNDÜREBİLİRİZ.
        // return "YOUR_VALID_JWT_TOKEN_FOR_TESTING" // Test için
        return null
    }

    /**
     * Uygulama ön planda veya arka planda iken FCM'den mesaj alındığında çağrılır.
     * Sunucudan gönderilen 'data' mesajlarını işler.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Gelen mesajın data payload'ını kontrol et
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val title = remoteMessage.data["title"] ?: "Yeni Bildirim"
            val body = remoteMessage.data["body"] ?: "Detaylar için dokunun."
            // Sunucudan ek veri geliyorsa (örn: flightId, searchId) buradan alabilirsin
            // val flightId = remoteMessage.data["flightId"]
            // val searchId = remoteMessage.data["searchId"]

            // Bildirimi göster
            sendNotification(title, body, remoteMessage.data)

            // Opsiyonel: Gelen bildirimi yerel veritabanına kaydet
            // saveNotificationToLocalDb(title, body, remoteMessage.data)
        }

        // Eğer mesaj notification payload içeriyorsa (sunucudan notification mesajı gönderilirse)
        // remoteMessage.notification?.let {
        //     Log.d(TAG, "Message Notification Body: ${it.body}")
        //     sendNotification(it.title ?: "Travel Assistant", it.body ?: "Yeni bir bildiriminiz var.", remoteMessage.data)
        // }
    }

    /**
     * Alınan FCM mesajı için basit bir bildirim oluşturur ve gösterir.
     */
    private fun sendNotification(title: String, messageBody: String, data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        // Eğer özel bir ekrana yönlendirme yapılacaksa, data içerisinden alınan ID'ler intent'e eklenebilir
        // data.forEach { (key, value) -> intent.putExtra(key, value) }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.default_notification_channel_id) // strings.xml içinde tanımlanmalı
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Uygulama ikonunuz
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo (API 26) ve sonrası için Notification Channel gereklidir.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Genel Bildirimler", // Kanal adı strings.xml içinde tanımlanabilir
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Date().time.toInt() /* ID to notify */, notificationBuilder.build())
    }

    /*
    // Opsiyonel: Bildirimi yerel veritabanına kaydetme fonksiyonu
    private fun saveNotificationToLocalDb(title: String, message: String, data: Map<String, String>) {
        coroutineScope.launch {
            try {
                val newNotification = LocalNotificationEntity(
                    id = UUID.randomUUID().toString(), // Ya da sunucudan gelen bir ID varsa o kullanılır
                    userId = "current_user_id", // Mevcut kullanıcı ID'si alınmalı
                    searchId = data["searchId"] ?: "",
                    flightId = data["flightId"] ?: "",
                    title = title,
                    message = message,
                    createdAt = System.currentTimeMillis(),
                    readAt = null
                )
                // notificationRepository.insertNotification(newNotification)
                Log.d(TAG, "Bildirim yerel DB'ye kaydedildi (placeholder).")
            } catch (e: Exception) {
                Log.e(TAG, "Bildirimi DB'ye kaydederken hata oluştu", e)
            }
        }
    }
    */
}