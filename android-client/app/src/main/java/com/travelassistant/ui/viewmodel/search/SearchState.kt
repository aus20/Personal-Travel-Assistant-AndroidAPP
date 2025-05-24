package com.travelassistant.ui.viewmodel.search

import com.travelassistant.data.model.FlightResult
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
        val flights: List<FlightResult>,
        val filters: FlightFilters,
        val sortOption: SortOption,
        val availableAirlines: List<String>
    ) : SearchResultsState()
}

data class FlightFilters(
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1000f, // FilterSheet ile aynı varsayılan
    val maxStops: Int = 2, // FilterSheet ile aynı varsayılan
    val selectedAirlines: List<String> = emptyList() // FilterSheet ile aynı tip ve varsayılan
)

enum class SortOption {
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    DURATION_SHORT_TO_LONG,
    DEPARTURE_TIME_EARLY_TO_LATE
}