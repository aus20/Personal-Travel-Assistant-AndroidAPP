package com.travelassistant.data.model

data class FlightInfo(
    val airline: String,
    val flightNumber: String,
    val departureTime: String,
    val departureAirport: String,
    val arrivalTime: String,
    val arrivalAirport: String,
    val price: Double? = null
) 