package com.travelassistant.ui.viewmodel.saved

import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.travelassistant.data.model.toFlightResult
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SavedSearchesViewModel @Inject constructor(
    private val flightSearchRepository: FlightSearchRepository,
    private val flightResultRepository: FlightResultRepository,
    private val networkStateManager: NetworkStateManager
) : BaseUiViewModel<SavedSearchesState, SavedSearchesEvent>() {

    override val _state = MutableStateFlow<UiState<SavedSearchesState>>(UiState.Initial)

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        viewModelScope.launch {
            networkStateManager.isOnline.collect { isOnline ->
                _isOnline.value = isOnline
            }
        }
        loadSavedSearches()
    }

    override fun onEvent(event: SavedSearchesEvent) {
        when (event) {
            is SavedSearchesEvent.LoadSavedSearches -> loadSavedSearches()
            is SavedSearchesEvent.DeleteSearch -> deleteSearch(event.searchId)
            is SavedSearchesEvent.RefreshSearchResults -> refreshSearchResults(event.searchId)
            is SavedSearchesEvent.ViewSearch -> handleViewSearch(event.searchId)
        }
    }

    private fun loadSavedSearches() {
        handleResult(
            block = {
                // TODO: Get current user ID from auth manager
                val userId = "current_user"
                val currentTime = System.currentTimeMillis()
                flightSearchRepository.getActiveSearches(userId, currentTime).first()
            },
            onSuccess = { searches ->
                updateState { currentState ->
                    val savedSearches = searches.map { entity ->
                        SavedSearch(
                            id = entity.id,
                            fromLocation = entity.origin,
                            toLocation = entity.destination,
                            departureDate = Date(entity.departureDate),
                            returnDate = entity.returnDate?.let { Date(it) },
                            passengerCount = 1, // TODO: Add to entity
                            lastSearchResults = null, // Will be loaded separately if needed
                            createdAt = Date(entity.createdAt),
                            isActive = true
                        )
                    }
                    UiState.Success(SavedSearchesState.Success(savedSearches = savedSearches))
                }
            }
        )
    }

    private fun deleteSearch(searchId: String) {
        handleResult(
            block = {
                val search = flightSearchRepository.getSearchById(searchId)
                search?.let {
                    flightSearchRepository.deleteSearch(it)
                }
            },
            onSuccess = {
                loadSavedSearches()
            }
        )
    }

    private fun refreshSearchResults(searchId: String) {
        handleResult(
            block = {
                val search = flightSearchRepository.getSearchById(searchId)
                if (search == null) {
                    throw IllegalStateException("Search not found")
                }

                // Get flight results for this search
                flightResultRepository.getFlightResultsBySearchId(searchId).first()
            },
            onSuccess = { results ->
                updateState { currentUiState ->
                    when (currentUiState) {
                        is UiState.Success -> {
                            // currentUiState.data is of type SavedSearchesState
                            val currentSpecificState = currentUiState.data
                            if (currentSpecificState is SavedSearchesState.Success) {
                                // Now currentSpecificState.savedSearches is accessible
                                val updatedSearches = currentSpecificState.savedSearches.map { savedSearch ->
                                    if (savedSearch.id == searchId) {
                                        // Ensure 'results' is a List and 'it.toFlightResult()' is valid
                                        savedSearch.copy(lastSearchResults = results.map { it.toFlightResult() })
                                    } else {
                                        savedSearch
                                    }
                                }
                                // Return a new UiState.Success wrapping the updated SavedSearchesState.Success
                                UiState.Success(currentSpecificState.copy(savedSearches = updatedSearches))
                            } else {
                                // If UiState.Success holds a SavedSearchesState that isn't .Success
                                // (e.g. UiState.Success(SavedSearchesState.Loading)), return current state.
                                currentUiState
                            }
                        }
                        else -> currentUiState // For UiState.Initial, UiState.Loading, UiState.Error
                    }
                }
            }
        )
    }

    private fun handleViewSearch(searchId: String) {
        // TODO: Navigate to search results screen
    }
} 