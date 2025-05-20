package com.travelassistant.data.remote.dto.request
// Server'a kullanıcı girişi için gönderilecek veri sınıfı
// Bu sınıf, kullanıcıdan alınan bilgileri temsil eder ve sunucuya gönderilecek JSON formatında yapılandırılır.
import com.google.gson.annotations.SerializedName

data class UserLoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
