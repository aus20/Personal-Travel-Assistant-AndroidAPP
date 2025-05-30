package com.travelassistant.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class SaveFlightSearchRequest(
    @SerializedName("origin")
    val origin: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("departureDate")
    val departureDate: String, // "yyyy-MM-dd" formatında
    @SerializedName("returnDate")
    val returnDate: String?, // "yyyy-MM-dd" formatında, opsiyonel
    @SerializedName("adults")
    val adults: Int
)