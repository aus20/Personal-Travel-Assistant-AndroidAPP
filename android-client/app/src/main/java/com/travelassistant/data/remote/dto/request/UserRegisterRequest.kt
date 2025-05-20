package com.travelassistant.data.remote.dto.request

import com.google.gson.annotations.SerializedName
// Server'a kullanıcı kaydı için gönderilecek veri sınıfı
// Bu sınıf, kullanıcıdan alınan bilgileri temsil eder ve sunucuya gönderilecek JSON formatında yapılandırılır.
// Kullanıcıdan alınacak bilgiler: ad, e-posta ve şifre
data class UserRegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)