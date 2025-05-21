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
import com.travelassistant.data.session.SessionManager
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

    @Inject // SessionManager'ı Hilt ile enjekte et
    lateinit var sessionManager: SessionManager

    private val tag = "MyFirebaseMsgService"
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * FCM registration token her değiştiğinde çağrılır.
     * Bu token, uygulamanın bu cihaza bildirim alabilmesi için gereklidir.
     */
    override fun onNewToken(token: String) {
        Log.d(tag, "Refreshed token: $token")
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
                    Log.w(tag, "Kullanıcı giriş yapmamış veya JWT token bulunamadı. FCM token gönderilemiyor.")
                    return@launch
                }

                // FcmTokenRequest objesini burada oluşturuyoruz
                val requestDto = FcmTokenRequest(token = fcmToken)

                // AuthRepository (veya UserRepository) üzerinden token güncelleme
                when (val result = authRepository.updateFcmToken(jwtAuthToken, requestDto)) {
                    is Result.Success -> {
                        Log.i(tag, "FCM Token sunucuya başarıyla gönderildi/güncellendi.")
                    }
                    is Result.Error -> {
                        Log.e(tag, "FCM Token sunucuya gönderilirken hata oluştu: ${result.message}")
                        // Gerekirse tekrar deneme mekanizması (WorkManager vb.)
                    }
                    else -> { /* Result.Loading durumu ele alınabilir */ }
                }
            }
        }
    }
    // Placeholder: Gerçek JWT token'ı alma mantığı buraya gelmeli
    private suspend fun retrieveCurrentAuthToken(): String? {
        val token = sessionManager.getAuthToken()
        if (token.isNullOrBlank()) {
            Log.w(tag, "retrieveCurrentAuthToken() - SessionManager'dan JWT token bulunamadı.")
        } else {
            Log.d(tag, "retrieveCurrentAuthToken() - SessionManager'dan JWT token başarıyla alındı.")
        }
        return token
    }

    /**
     * Uygulama ön planda veya arka planda iken FCM'den mesaj alındığında çağrılır.
     * Sunucudan gönderilen 'data' mesajlarını işler.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(tag, "From: ${remoteMessage.from}")

        // Gelen mesajın data payload'ını kontrol et
        remoteMessage.data.isNotEmpty().let {
            Log.d(tag, "Message data payload: " + remoteMessage.data)

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
    // bu metod fuckedup bullshit baya bakmak gerekecek.
    private fun sendNotification(title: String, messageBody: String, data: Map<String, String>) {
        // 1. Bildirime tıklandığında açılacak Intent'i oluştur
        val intent = Intent(this, MainActivity::class.java).apply {
            // FLAG_ACTIVITY_CLEAR_TOP: Eğer MainActivity zaten açıksa, onun üzerine yeni bir tane açmak yerine
            // mevcut olanı en üste getirir ve üzerindeki diğer Activity'leri temizler.
            // Bu, kullanıcı geri gittiğinde beklenmedik Activity yığınlarıyla karşılaşmasını engeller.
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // İleride, bildirime özel verileri (örn: hangi ekrana gidileceği, bir ID vb.)
            // 'data' Map'inden alıp buraya 'putExtra' ile ekleyebilirsin.
            // Örnek:
            // val screenToOpen = data["screen"]
            // if (screenToOpen != null) {
            //     putExtra("TARGET_SCREEN", screenToOpen)
            // }
            // val itemId = data["item_id"]
            // if (itemId != null) {
            //     putExtra("ITEM_ID", itemId)
            // }
        }

        // 2. PendingIntent oluştur (Intent'i daha sonra çalıştırılmak üzere paketler)
        // Android S (API 31) ve üzeri için FLAG_IMMUTABLE veya FLAG_MUTABLE belirtmek zorunludur.
        // FLAG_IMMUTABLE genellikle güvenlik için daha iyidir.
        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_ONE_SHOT
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, // Request code (birden fazla PendingIntent varsa ayırt etmek için kullanılır, şimdilik 0 yeterli)
            intent,
            pendingIntentFlag
        )

        // 3. Bildirim Kanalı ID'sini ve Adını strings.xml'den al
        // Bu string'lerin strings.xml dosyasında tanımlı olduğundan emin olmalısın.
        // <string name="default_notification_channel_id" translatable="false">travel_assistant_default_channel</string>
        // <string name="default_notification_channel_name">Genel Bildirimler</string>
        val channelId = getString(R.string.default_notification_channel_id) //
        val channelName = getString(R.string.default_notification_channel_name) //

        // 4. Varsayılan bildirim sesini al
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 5. NotificationCompat.Builder ile bildirimi yapılandır
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Uygulama ikonun (Status bar için transparan arkaplanlı beyaz bir ikon daha iyi görünür)
            .setContentTitle(title)             // FCM mesajından gelen başlık
            .setContentText(messageBody)        // FCM mesajından gelen içerik
            .setAutoCancel(true)                // Kullanıcı tıkladığında bildirimin otomatik kapanmasını sağlar
            .setSound(defaultSoundUri)          // Bildirim sesi
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Bildirimin önem derecesi (acil bildirimler için)
            .setContentIntent(pendingIntent)    // Kullanıcı bildirime tıkladığında çalışacak PendingIntent

        // 6. NotificationManager'ı al
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 7. Android Oreo (API 26) ve sonrası için Bildirim Kanalı oluştur (eğer henüz oluşturulmadıysa)
        // Bildirim kanalları, kullanıcıların hangi tür bildirimleri alacaklarını yönetmelerini sağlar.
        // Bu kontrol sadece bir kez (kanal ilk defa gerektiğinde) kanal oluşturur.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH // Kanalın önem derecesi
            )
            // İsteğe bağlı: Kanala açıklama, titreşim ayarı vb. eklenebilir.
            // channel.description = "Uygulamanın genel bildirimleri için kanal."
            notificationManager.createNotificationChannel(channel)
        }

        // 8. Bildirimi göster
        // Her bildirim için farklı bir ID kullanmak (örn: mevcut zaman),
        // yeni bildirimlerin eskilerinin üzerine yazmasını engeller.
        // Eğer belirli bir bildirim türü için eskisini güncellemek istersen,
        // o türe özel sabit bir ID kullanabilirsin.
        notificationManager.notify(Date().time.toInt(), notificationBuilder.build())

        Log.d("MyFirebaseMsgService", "Bildirim gönderildi: $title - $messageBody")
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