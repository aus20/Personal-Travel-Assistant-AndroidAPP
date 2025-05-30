package com.travelassistant.ui.viewmodel.saved

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import com.travelassistant.data.network.NetworkStateManager
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.data.repository.SavedSearchRepository
import com.travelassistant.data.session.SessionManager
import com.travelassistant.ui.viewmodel.BaseUiViewModel
import com.travelassistant.ui.viewmodel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class SavedSearchesViewModel @Inject constructor(
    private val flightSearchRepository: FlightSearchRepository,
    private val flightResultRepository: FlightResultRepository,
    private val networkStateManager: NetworkStateManager,
    private val savedSearchRepository: SavedSearchRepository,
    internal val sessionManager: SessionManager
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

        val userDetails = sessionManager.getUserDetails()

        handleResult(
            block = {
                // TODO: Get current user ID from auth manager
                val userId = userDetails?.id
                val currentTime = System.currentTimeMillis()
                // Collect the first emission from the Flow, which will be the list
                flightSearchRepository.getActiveSearches(userId, currentTime).first()
            },
            onSuccess = { searches -> // 'searches' will now be List<SavedFlightSearchEntity>
                updateState { currentState ->
                    val savedSearches = searches.map { entity ->
                        SavedSearch(
                            id = entity.id,
                            fromLocation = entity.origin,
                            toLocation = entity.destination,
                            departureDate = entity.departureDate,
                            returnDate = entity.returnDate,
                            passengerCount = entity.passengers, // TODO: Add to entity
                            lastSearchResults = null, // Will be loaded separately if needed
                            createdAt = entity.createdAt.toString(),
                            isActive = true
                        )
                    }
                    UiState.Success(SavedSearchesState.Success(data = SavedSearchData(savedSearches = savedSearches)))
                }
            }
        )
    }

    private fun deleteSearch(searchId: String) {
        /*
        handleResult(
            block = {
                flightSearchRepository.getSearchById(searchId)?.let {
                    flightSearchRepository.deleteSearch(it)
                }
            },
            onSuccess = {
                loadSavedSearches()
            }
        )

         */

        Log.d("SavedSearchesVM", "Attempting to delete search with ID: $searchId")
        handleResult(
            block = {
                // 1. Convert searchId: String to searchId: Long for the backend API call.
                val searchIdLong: Long
                try {
                    searchIdLong = searchId.toLong()
                } catch (e: NumberFormatException) {
                    Log.e("SavedSearchesVM", "Invalid searchId format '$searchId'. Cannot convert to Long.", e)
                    // Throw an exception to be caught by handleResult's error handling
                    throw IllegalArgumentException("Invalid search ID format.")
                }

                // 2. Call the backend to delete the search.
                Log.d("SavedSearchesVM", "Calling backend to delete search ID: $searchIdLong")
                val backendResult = savedSearchRepository.deleteSavedFlightSearch(searchIdLong)

                when (backendResult) {
                    is com.travelassistant.util.Result.Success -> {
                        Log.i("SavedSearchesVM", "Search successfully deleted from backend: ${backendResult.data.message}")

                        // 3. If backend deletion was successful, delete from local Room database.
                        Log.d("SavedSearchesVM", "Attempting to delete search from Room with ID: $searchId")
                        val localEntity = flightSearchRepository.getSearchById(searchId)
                        if (localEntity != null) {
                            flightSearchRepository.deleteSearch(localEntity)
                            Log.i("SavedSearchesVM", "Search successfully deleted from Room.")
                        } else {
                            // This might not be an error; perhaps it was already deleted or never synced.
                            Log.w("SavedSearchesVM", "Search with ID $searchId not found in Room for local deletion, but backend deletion was successful.")
                        }
                        // The block successfully completed its tasks.
                        // Return Unit or any specific success marker if needed by onSuccess.
                        Unit // Signifies success of this block
                    }
                    is com.travelassistant.util.Result.Error -> {
                        Log.e("SavedSearchesVM", "Failed to delete search from backend: ${backendResult.message}")
                        // Throw an exception to be caught by handleResult's error handling
                        // This will prevent onSuccess (and loadSavedSearches) from being called.
                        throw Exception("Backend deletion failed: ${backendResult.message}")
                    }

                    com.travelassistant.util.Result.Loading -> TODO()
                }
            },
            onSuccess = {
                // This block is executed only if the `block` above completes without throwing an exception.
                Log.i("SavedSearchesVM", "Both backend and local (if found) deletions were successful for search ID: $searchId. Reloading searches.")
                loadSavedSearches()
                // You might want to emit a success message to the UI here, e.g., through a StateFlow/LiveData
                // _viewState.value = SomeUiState.SuccessMessage("Search deleted successfully!")
            },
            onError = { exception ->
                // This block is executed if `block` throws an exception (e.g., backend failure, ID format error).
                Log.e("SavedSearchesVM", "Delete operation failed for search ID $searchId: ${exception.message}", exception)
                // Update UI to show an error message
                // _viewState.value = SomeUiState.ErrorMessage("Failed to delete search: ${exception.message}")
            }
        )
    }


    private fun refreshSearchResults(searchId: String) {
        handleResult(
            block = {
                flightSearchRepository.getSearchById(searchId)
            },
            onSuccess = { results -> // 'results' will now be List<SomeEntityType>
                updateState { currentUiState -> // currentUiState is UiState<SavedSearchesState>
                    currentUiState
                }
            }
        )
    }

    private fun handleViewSearch(searchId: String) {
        // TODO: Navigate to search results screen
    }
}

private fun CachedFlightResultEntity.toFlightResult() {
    TODO("Not yet implemented")
}
