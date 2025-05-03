package com.travelassistant.data.model

import java.util.Date

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