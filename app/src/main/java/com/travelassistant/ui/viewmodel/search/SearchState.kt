package com.travelassistant.ui.viewmodel.search

import com.travelassistant.data.remote.dto.response.FlightSearchResponse
import java.util.Date

sealed class SearchState {
    data object Initial : SearchState()
    data object Loading : SearchState()
    data class Error(val message: String) : SearchState()
    data class Success(
        val fromLocation: String,
        val toLocation: String,
        val departureDate: Date?,
        val returnDate: Date?,
        val passengerCount: Int,
        val isFormValid: Boolean
    ) : SearchState()
}

sealed class SearchResultsState {
    data object Initial : SearchResultsState()
    data object Loading : SearchResultsState()
    data class Error(val message: String) : SearchResultsState()
    data class Success(
        val flights: FlightSearchResponse,
    ) : SearchResultsState()
}

data class FlightFilters(
    val priceRange: ClosedFloatingPointRange<Float> = 0f..Float.MAX_VALUE,
    val maxStops: Int = Int.MAX_VALUE,
    val selectedAirlines: Set<String> = emptySet()
)

enum class SortOption {
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    DURATION_SHORT_TO_LONG,
    DEPARTURE_TIME_EARLY_TO_LATE
} 