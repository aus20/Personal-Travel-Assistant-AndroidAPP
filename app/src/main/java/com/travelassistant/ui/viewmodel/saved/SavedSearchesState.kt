package com.travelassistant.ui.viewmodel.saved

import java.util.Date

data class SavedSearchData(
    val savedSearches: List<SavedSearch>,
    val error: String? = null,
    val isLoading: Boolean = false
)

data class SavedSearch(
    val id: String,
    val fromLocation: String,
    val toLocation: String,
    val departureDate: String,
    val returnDate: String?,
    val passengerCount: Int,
    val lastSearchResults: List<Unit>?,
    val createdAt: String,
    val isActive: Boolean
)

sealed class SavedSearchesState {
    data object Initial : SavedSearchesState()
    data object Loading : SavedSearchesState()
    data class Error(val message: String) : SavedSearchesState()
    data class Success(
        val data : SavedSearchData
    ) : SavedSearchesState()
} 