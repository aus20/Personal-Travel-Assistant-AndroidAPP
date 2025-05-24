package com.travelassistant.data.model

import com.travelassistant.data.local.entity.CachedFlightResultEntity
import java.util.Date
import java.util.concurrent.TimeUnit

data class FlightResult(
    val id: String,
    val airline: String,
    val airlineLogo: String,
    val flightNumber: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureTime: Date,
    val arrivalTime: Date,
    val duration: String,
    val stops: Int,
    val price: Double,
    val currency: String = "USD"
)
fun CachedFlightResultEntity.toFlightResult(): com.travelassistant.data.model.FlightResult {
    val departure = Date(this.departureTime)
    val arrival = Date(this.arrivalTime)
    val diffInMillis = arrival.time - departure.time
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

    return com.travelassistant.data.model.FlightResult(
        id = this.id,
        airline = this.airline,
        airlineLogo = "", // Placeholder: You'll need a way to get this, e.g., based on airline name
        flightNumber = this.flightNumber,
        departureAirport = "", // Placeholder: This info is not in CachedFlightResultEntity
        arrivalAirport = "",   // Placeholder: This info is not in CachedFlightResultEntity
        departureTime = departure,
        arrivalTime = arrival,
        duration = String.format("%dh %02dm", hours, minutes),
        stops = this.stops,
        price = this.price
        // currency has a default value
    )
}