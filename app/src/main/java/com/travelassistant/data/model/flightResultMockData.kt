package com.travelassistant.data.model

import com.google.gson.annotations.SerializedName
import com.travelassistant.data.remote.dto.response.FlightLegResponse
import com.travelassistant.data.remote.dto.response.FlightSearchResponse


val searchResults = FlightSearchResponse(
    departureFlight = listOf(
        FlightLegResponse(
            origin = "Istanbul",
            destination = "London",
            originAirportCode = "ISTM",
            destinationAirportCode = "LONK",
            layoverAirports = emptyList(),
            departureTime = "2025-07-15T14:47:00",
            arrivalTime = "2025-07-15T19:04:00",
            carrier = "TK 4301",
            duration = "4H17M",
            aircraftCode = "321",
            cabinClass = "ECONOMY",
            numberOfStops = 0,
            price = 98.77007561688791,
            currency = "USD",
            leg = "DEPARTURE"
        ),
        FlightLegResponse(
            origin = "Istanbul",
            destination = "London",
            originAirportCode = "ISTM",
            destinationAirportCode = "LONK",
            layoverAirports = emptyList(),
            departureTime = "2025-07-15T19:32:00",
            arrivalTime = "2025-07-15T21:32:00",
            carrier = "PC 9878",
            duration = "2H0M",
            aircraftCode = "77W",
            cabinClass = "ECONOMY",
            numberOfStops = 0,
            price = 127.73848116118613,
            currency = "USD",
            leg = "DEPARTURE"
        ),
        FlightLegResponse(
            origin = "Istanbul",
            destination = "London",
            originAirportCode = "ISTM",
            destinationAirportCode = "LONK",
            layoverAirports = listOf("VIE", "VIE"),
            departureTime = "2025-07-15T14:54:00",
            arrivalTime = "2025-07-15T16:10:00",
            carrier = "MOCK 1189",
            duration = "1H16M",
            aircraftCode = "738",
            cabinClass = "BUSINESS",
            numberOfStops = 2,
            price = 372.319346282696,
            currency = "USD",
            leg = "DEPARTURE"
        )
    )
)
