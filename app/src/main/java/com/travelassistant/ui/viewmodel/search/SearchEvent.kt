package com.travelassistant.ui.viewmodel.search

import java.util.Date

sealed class SearchEvent {
    data class UpdateFromLocation(val fromLocation: String) : SearchEvent()
    data class UpdateToLocation(val toLocation: String) : SearchEvent()
    data class UpdateDepartureDate(val departureDate: String) : SearchEvent()
    data class UpdateReturnDate(val returnDate: String?) : SearchEvent()
    data class UpdatePassengerCount(val passengerCount: Int) : SearchEvent()
    data class UpdateFilters(val filters: FlightFilters) : SearchEvent()
    data class UpdateSortOption(val sortOption: SortOption) : SearchEvent()
} 