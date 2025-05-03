package com.travelassistant.ui.screens.saved

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.cards.FlightCard
import com.travelassistant.ui.components.common.states.EmptyState

// Sample data for preview
private val sampleSearches = listOf(
    Triple("New York", "London", "Mar 15 - Mar 19, 2024"),
    Triple("Tokyo", "Paris", "Apr 20 - Apr 30, 2024"),
    Triple("Sydney", "Dubai", "May 10 - May 25, 2024")
)

@Composable
fun SavedSearchesScreen() {
    var savedSearches by remember { mutableStateOf(sampleSearches) }
    
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
                    items(savedSearches) { (from, to, date) ->
                        FlightCard(
                            fromCity = from,
                            toCity = to,
                            date = date,
                            price = "400$",
                            onDelete = {
                                savedSearches = savedSearches.filter { it != Triple(from, to, date) }
                            }
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
    SavedSearchesScreen()
} 