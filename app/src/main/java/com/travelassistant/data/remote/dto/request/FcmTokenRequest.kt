package com.travelassistant.data.remote.dto.request
import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("token")
    val token: String
)
