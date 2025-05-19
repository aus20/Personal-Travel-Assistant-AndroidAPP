package com.travelassistant.ui.viewmodel.saved

sealed class SavedSearchesEvent {
    object LoadSavedSearches : SavedSearchesEvent()
    data class DeleteSearch(val searchId: String) : SavedSearchesEvent()
    data class RefreshSearchResults(val searchId: String) : SavedSearchesEvent()
    data class ViewSearch(val searchId: String) : SavedSearchesEvent()
} 