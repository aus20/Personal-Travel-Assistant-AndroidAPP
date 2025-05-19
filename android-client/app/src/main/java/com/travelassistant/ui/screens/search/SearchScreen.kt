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
    fromLocation: String,
    toLocation: String,
    departureDate: Date?,
    returnDate: Date?,
    passengerCount: Int,
    onFromLocationChange: (String) -> Unit,
    onToLocationChange: (String) -> Unit,
    onDepartureDateChange: (Date?) -> Unit,
    onReturnDateChange: (Date?) -> Unit,
    onPassengerCountChange: (Int) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        contentDescription = "Flight Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Find Your Perfect Flight",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            FlightSearchForm(
                fromLocation = fromLocation,
                toLocation = toLocation,
                departureDate = departureDate,
                returnDate = returnDate,
                passengerCount = passengerCount,
                onFromLocationChange = onFromLocationChange,
                onToLocationChange = onToLocationChange,
                onDepartureDateSelected = onDepartureDateChange,
                onReturnDateSelected = onReturnDateChange,
                onPassengerCountChange = onPassengerCountChange
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PrimaryButton(
                text = "Search Flights",
                onClick = onSearchClick,
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
        fromLocation = "",
        toLocation = "",
        departureDate = null,
        returnDate = null,
        passengerCount = 1,
        onFromLocationChange = {},
        onToLocationChange = {},
        onDepartureDateChange = {},
        onReturnDateChange = {},
        onPassengerCountChange = {},
        onSearchClick = {}
    )
}