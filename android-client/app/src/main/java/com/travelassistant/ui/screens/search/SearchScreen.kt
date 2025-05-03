package com.travelassistant.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.buttons.PrimaryButton
import com.travelassistant.ui.components.features.search.FlightSearchForm
import java.util.Date

@Composable
fun SearchScreen(
    onSearchResults: (String, String, Date, Date?) -> Unit,
    modifier: Modifier = Modifier
) {
    var fromLocation by remember { mutableStateOf("") }
    var toLocation by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf<Date?>(null) }
    var returnDate by remember { mutableStateOf<Date?>(null) }
    var passengerCount by remember { mutableStateOf(1) }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Header with icon
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FlightTakeoff,
                        contentDescription = null,
                        modifier = Modifier.padding(bottom = 8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "Find Your Flight",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Search for flights and track prices",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search Form
            FlightSearchForm(
                fromLocation = fromLocation,
                onFromLocationChange = { fromLocation = it },
                toLocation = toLocation,
                onToLocationChange = { toLocation = it },
                departureDate = departureDate,
                onDepartureDateSelected = { departureDate = it },
                returnDate = returnDate,
                onReturnDateSelected = { returnDate = it },
                passengerCount = passengerCount,
                onPassengerCountChange = { passengerCount = it }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Search Button
            PrimaryButton(
                text = "Search Flights",
                onClick = { 
                    // Navigate to search results screen
                    departureDate?.let { date ->
                        onSearchResults(fromLocation, toLocation, date, returnDate)
                    }
                },
                enabled = fromLocation.isNotBlank() && toLocation.isNotBlank() && departureDate != null
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        onSearchResults = { _, _, _, _ -> }
    )
}