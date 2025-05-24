package com.travelassistant.ui.viewmodel.search

import java.util.Date

data class SearchData(
    val fromLocation: String = "",
    val toLocation: String = "",
    val departureDate: Date? = null,
    val returnDate: Date? = null,
    val passengerCount: Int = 1,
    val isFormValid: Boolean = false,
    val searchResults: List<Any> = emptyList(),
    val availableAirlines: List<String> = emptyList(),
    val filters: FlightFilters = FlightFilters(),
    val sortOption: SortOption = SortOption.PRICE_LOW_TO_HIGH
) 