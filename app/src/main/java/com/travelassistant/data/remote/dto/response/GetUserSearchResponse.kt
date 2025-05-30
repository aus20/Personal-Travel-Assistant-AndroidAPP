package com.travelassistant.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class GetUserSearchResponse(
    @SerializedName("searchId") // Sunucudaki ID alanının adı (örn: "id", "search_id")
    val searchId: Long,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("departureDate")
    val departureDate: String, // "yyyy-MM-dd" formatında
    @SerializedName("returnDate")
    val returnDate: String?,   // "yyyy-MM-dd" formatında, opsiyonel
    @SerializedName("adults")
    val adults: Int
    // Sunucudan gelen ek alanlar varsa (örn: createdAt, isTrackingActive vb.) buraya eklenebilir
)