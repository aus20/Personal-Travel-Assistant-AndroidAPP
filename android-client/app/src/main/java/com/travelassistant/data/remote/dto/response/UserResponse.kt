package com.travelassistant.data.remote.dto.response // veya .response

import com.google.gson.annotations.SerializedName
// Server'dan gelen yanıtları temsil eden veri sınıfı
// Bu sınıf, sunucudan gelen JSON yanıtını temsil eder.
data class UserResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String
)