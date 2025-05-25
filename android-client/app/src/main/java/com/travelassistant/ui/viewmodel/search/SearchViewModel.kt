package com.travelassistant.ui.viewmodel.search

import androidx.lifecycle.viewModelScope
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.data.model.FlightResult
import com.travelassistant.data.model.toFlightResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val flightSearchRepository: FlightSearchRepository,
    private val flightResultRepository: FlightResultRepository,
    private val networkStateManager: NetworkStateManager
) : BaseUiViewModel<SearchData, SearchEvent>() {

    override val _state = MutableStateFlow<UiState<SearchData>>(UiState.Initial)

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        viewModelScope.launch {
            networkStateManager.isOnline.collect { isOnline ->
                _isOnline.value = isOnline
            }
        }
        _state.value = UiState.Success(SearchData())
    }

    override fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateFromLocation -> updateFromLocation(event.fromLocation)
            is SearchEvent.UpdateToLocation -> updateToLocation(event.toLocation)
            is SearchEvent.UpdateDepartureDate -> updateDepartureDate(event.departureDate)
            is SearchEvent.UpdateReturnDate -> updateReturnDate(event.returnDate)
            is SearchEvent.UpdatePassengerCount -> updatePassengerCount(event.passengerCount)
            is SearchEvent.PerformSearch -> performSearch()
            is SearchEvent.UpdateFilters -> updateFilters(event.filters)
            is SearchEvent.UpdateSortOption -> updateSortOption(event.sortOption)
        }
    }

    private fun updateFromLocation(fromLocation: String) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(
                        fromLocation = fromLocation,
                        isFormValid = validateForm(
                            fromLocation = fromLocation,
                            toLocation = currentState.data.toLocation,
                            departureDate = currentState.data.departureDate
                        )
                    )
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun updateToLocation(toLocation: String) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(
                        toLocation = toLocation,
                        isFormValid = validateForm(
                            fromLocation = currentState.data.fromLocation,
                            toLocation = toLocation,
                            departureDate = currentState.data.departureDate
                        )
                    )
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun updateDepartureDate(departureDate: Date?) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(
                        departureDate = departureDate,
                        isFormValid = validateForm(
                            fromLocation = currentState.data.fromLocation,
                            toLocation = currentState.data.toLocation,
                            departureDate = departureDate
                        )
                    )
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun updateReturnDate(returnDate: Date?) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(returnDate = returnDate)
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun updatePassengerCount(passengerCount: Int) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(passengerCount = passengerCount)
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun performSearch() {
        val currentState = _state.value
        if (currentState !is UiState.Success || !currentState.data.isFormValid) {
            return
        }

        handleResult(
            block = {
                // Create a new search entity
                val searchEntity = SavedFlightSearchEntity(
                    id = "", // Will be generated by the repository
                    userId = "current_user", // TODO: Get from auth manager
                    origin = currentState.data.fromLocation,
                    destination = currentState.data.toLocation,
                    departureDate = currentState.data.departureDate?.time ?: 0L,
                    returnDate = currentState.data.returnDate?.time,
                    maxPrice = if (currentState.data.filters.priceRange.endInclusive == Float.MAX_VALUE) Double.MAX_VALUE else currentState.data.filters.priceRange.endInclusive.toDouble(),
                    preferredAirlines = currentState.data.filters.selectedAirlines.toList(),
                    maxStops = currentState.data.filters.maxStops,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    lastSyncedAt = 0L
                )

                // Save the search
                val searchId = flightSearchRepository.insertSearch(searchEntity)

                // Get flight results
                flightResultRepository.getFlightResultsBySearchId(searchId).first()
            },
            onSuccess = { results ->
                updateState { currentUiState ->
                    when (currentUiState) {
                        is UiState.Success -> {
                            val newData = currentState.data.copy(
                                searchResults = results.map { it.toFlightResult() }
                            )
                            UiState.Success(newData)
                        }
                        else -> currentState
                    }
                }
            }
        )
    }

    private fun updateFilters(filters: FlightFilters) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(filters = filters)
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun updateSortOption(sortOption: SortOption) {
        updateState { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    val newData = currentState.data.copy(sortOption = sortOption)
                    UiState.Success(newData)
                }
                else -> currentState
            }
        }
    }

    private fun validateForm(
        fromLocation: String,
        toLocation: String,
        departureDate: Date?
    ): Boolean {
        return fromLocation.isNotBlank() &&
                toLocation.isNotBlank() &&
                departureDate != null &&
                fromLocation != toLocation
    }
}