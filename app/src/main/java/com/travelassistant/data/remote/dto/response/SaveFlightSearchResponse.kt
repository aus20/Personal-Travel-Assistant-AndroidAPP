package com.travelassistant.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class SaveFlightSearchResponse(
    @SerializedName("searchId")
    val searchId: Long, // Sunucudan gelen ID
    @SerializedName("message")
    val message: String,
    @SerializedName("origin")
    val origin: String,
    @SerializedName("destination")
    val destination: String,
    @SerializedName("departureDate")
    val departureDate: String,
    @SerializedName("returnDate")
    val returnDate: String?,
    @SerializedName("adults")
    val adults: Int
)