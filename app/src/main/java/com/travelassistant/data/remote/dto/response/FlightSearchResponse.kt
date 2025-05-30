package com.travelassistant.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


data class FlightSearchResponse(
    @SerializedName("departureFlight")
    val departureFlight: List<FlightLegResponse>
)

@Serializable
data class FlightLegResponse(
    @SerializedName("origin")
    val origin: String, // Şehir adı, örn: "Paris"
    @SerializedName("destination")
    val destination: String, // Şehir adı, örn: "Ankara"
    @SerializedName("originAirportCode")
    val originAirportCode: String, // "CDG"
    @SerializedName("destinationAirportCode")
    val destinationAirportCode: String, // "ESB"
    @SerializedName("layoverAirports")
    val layoverAirports: List<String>, // ["SAW", "EZS"]
    @SerializedName("departureTime")
    val departureTime: String, // "2025-07-15T17:10" (ISO 8601 formatında)
    @SerializedName("arrivalTime")
    val arrivalTime: String, // "2025-07-15T21:55" (ISO 8601 formatında)
    @SerializedName("carrier")
    val carrier: String, // "VF 516" (Havayolu ve uçuş numarası olabilir)
    @SerializedName("duration")
    val duration: String, // "3 hours 45 minutes"
    @SerializedName("aircraftCode")
    val aircraftCode: String, // "73H"
    @SerializedName("cabinClass")
    val cabinClass: String, // "ECONOMY"
    @SerializedName("numberOfStops")
    val numberOfStops: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("currency")
    val currency: String, // "EUR"
    @SerializedName("leg")
    val leg: String // "DEPARTURE" veya "RETURN"
    // Sunucudan gelen ek alanlar varsa buraya eklenebilir
    // Örn: Uçuş ID'si gibi bir alan varsa onu da eklemek faydalı olur
    // @SerializedName("flightId")
    // val flightId: String?
)