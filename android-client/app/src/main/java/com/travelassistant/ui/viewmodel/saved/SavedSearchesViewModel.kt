package com.travelassistant.ui.viewmodel.saved

import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
                flightSearchRepository.getActiveSearches(userId, currentTime)
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
                flightResultRepository.getFlightResultsBySearchId(searchId)
            },
            onSuccess = { results ->
                updateState { currentState ->
                    when (currentState) {
                        is UiState.Success -> {
                            val updatedSearches = currentState.data.savedSearches.map { savedSearch ->
                                if (savedSearch.id == searchId) {
                                    savedSearch.copy(lastSearchResults = results.map { it.toFlightResult() })
                                } else {
                                    savedSearch
                                }
                            }
                            UiState.Success(currentState.data.copy(savedSearches = updatedSearches))
                        }
                        else -> currentState
                    }
                }
            }
        )
    }

    private fun handleViewSearch(searchId: String) {
        // TODO: Navigate to search results screen
    }
} 