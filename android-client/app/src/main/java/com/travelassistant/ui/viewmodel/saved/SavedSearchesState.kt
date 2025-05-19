package com.travelassistant.ui.viewmodel.saved

import com.travelassistant.data.model.FlightResult
import java.util.Date

data class SavedSearch(
    val id: String,
    val fromLocation: String,
    val toLocation: String,
    val departureDate: Date,
    val returnDate: Date?,
    val passengerCount: Int,
    val lastSearchResults: List<FlightResult>?,
    val createdAt: Date,
    val isActive: Boolean
)

sealed class SavedSearchesState {
    data object Initial : SavedSearchesState()
    data object Loading : SavedSearchesState()
    data class Error(val message: String) : SavedSearchesState()
    data class Success(
        val savedSearches: List<SavedSearch>,
        val error: String? = null,
        val isLoading: Boolean = false
    ) : SavedSearchesState()
} 