package com.travelassistant.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class FlightSearchRequest(
    @SerializedName("origin")
    val origin: String, // kalkış şehri
    @SerializedName("destination")
    val destination: String, // iniş şehri
    @SerializedName("departureDate")
    val departureDate: String, // Örn: "2025-07-15" (Sunucunun beklediği formatta)
    @SerializedName("returnDate")
    val returnDate: String?, // Opsiyonel, Örn: "2025-07-22"
    @SerializedName("passengers")
    val passengers: Int,
    // Sunucuya göndereceğin ek filtreler varsa buraya eklenebilir
    // @SerializedName("maxStops")
    // val maxStops: Int?,
    // @SerializedName("cabinClass")
)