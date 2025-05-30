package com.travelassistant.ui.viewmodel.search

import com.travelassistant.data.model.FlightResult
import java.util.Date

data class SearchData(
    val fromLocation: String = "",
    val toLocation: String = "",
    val departureDate: String = "",
    val returnDate: String? = null,
    val passengerCount: Int = 1,
    val isFormValid: Boolean = false,
    val searchResults: List<FlightResult> = emptyList(),
    val availableAirlines: List<String> = emptyList(),
    val filters: FlightFilters = FlightFilters(),
    val sortOption: SortOption = SortOption.PRICE_LOW_TO_HIGH
)

data class SearchResultData(
    val fromCity: String = "",
    val toCity: String = "",
    val departureDate: Date? = null,
    val returnDate: Date? = null,
    val flights: List<FlightResult>,
    val isLoading: Boolean,
    val isOnline: Boolean,
    val currentFilters: FlightFilters,
    val selectedSortOption: SortOption,
    val showSortMenu: Boolean,
    val showFilterSheet: Boolean,
    val availableAirlines: List<String>,
    val onBackClick: () -> Unit,
    val onFlightClick: (FlightResult) -> Unit,
    val onSortOptionSelected: (SortOption) -> Unit,
    val onSortMenuVisibilityChanged: (Boolean) -> Unit,
    val onFilterSheetVisibilityChanged: (Boolean) -> Unit,
    val onFiltersApplied: (FlightFilters) -> Unit,
    val onFiltersReset: () -> Unit,
)

