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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.cards.FlightCard
import com.travelassistant.ui.components.common.states.EmptyState

data class SavedSearch(
    val from: String,
    val to: String,
    val date: String
)

@Composable
fun SavedSearchesScreen(
    savedSearches: List<SavedSearch>,
    onDeleteSearch: (SavedSearch) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier 
                .fillMaxSize()
                .padding(paddingValues)
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
                            fromCity = search.from,
                            toCity = search.to,
                            date = search.date,
                            price = "400$",
                            onDelete = { onDeleteSearch(search) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedSearchesScreenPreview() {
    SavedSearchesScreen(
        savedSearches = listOf(
            SavedSearch("New York", "London", "Mar 15 - Mar 19, 2024"),
            SavedSearch("Tokyo", "Paris", "Apr 20 - Apr 30, 2024"),
            SavedSearch("Sydney", "Dubai", "May 10 - May 25, 2024")
        ),
        onDeleteSearch = {}
    )
} 