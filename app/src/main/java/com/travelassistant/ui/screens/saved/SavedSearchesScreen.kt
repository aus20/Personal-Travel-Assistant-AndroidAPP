package com.travelassistant.ui.screens.saved

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.travelassistant.ui.components.common.cards.FlightCard
import com.travelassistant.ui.components.common.states.EmptyState
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.saved.SavedSearch
import com.travelassistant.ui.viewmodel.saved.SavedSearchesEvent
import com.travelassistant.ui.viewmodel.saved.SavedSearchesState
import com.travelassistant.ui.viewmodel.saved.SavedSearchesViewModel

@Composable
fun SavedSearchesScreen(
    onClickSevedItem: (SavedSearch) -> Unit,
    viewModel: SavedSearchesViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.onEvent(SavedSearchesEvent.LoadSavedSearches)
    }
    val uiState by viewModel.state.collectAsState()
    val savedSearches = when (val state = uiState) {
        is UiState.Error -> null
        UiState.Initial -> null
        UiState.Loading -> null
        is UiState.Success -> {
            // uiState.data is a SavedSearchesState
            when(val savedSearchesState = state.data) {
                is SavedSearchesState.Success -> savedSearchesState.data.savedSearches
                else -> null
            }
        }
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Saved Searches",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )

        if (savedSearches.isEmpty()) {
            EmptyState(
                icon = { /* Icon will be handled by EmptyState */ },
                title = "No Saved Searches",
                description = "Your saved flight searches will appear here. Start by searching for flights!"
            )
        } else {
            LazyColumn {
                items(savedSearches) { search ->
                    FlightCard(
                        search = search,
                        price = "150$",
                        onclick = onClickSevedItem,
                        onDelete = {  viewModel.onEvent(SavedSearchesEvent.DeleteSearch(search.id)) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedSearchesScreenPreview() {
    SavedSearchesScreen(
        onClickSevedItem = {}
    )
} 