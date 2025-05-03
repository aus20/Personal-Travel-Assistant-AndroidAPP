package com.travelassistant.ui.components.features.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.inputs.DateRangePicker
import com.travelassistant.ui.components.common.inputs.PassengerCountPicker
import com.travelassistant.ui.components.common.inputs.SearchBar
import java.util.Date

@Composable
fun FlightSearchForm(
    fromLocation: String,
    onFromLocationChange: (String) -> Unit,
    toLocation: String,
    onToLocationChange: (String) -> Unit,
    departureDate: Date? = null,
    onDepartureDateSelected: (Date) -> Unit = {},
    returnDate: Date? = null,
    onReturnDateSelected: (Date) -> Unit = {},
    passengerCount: Int = 1,
    onPassengerCountChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "From",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(
            value = fromLocation,
            onValueChange = onFromLocationChange,
            placeholder = "Enter departure city"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "To",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(
            value = toLocation,
            onValueChange = onToLocationChange,
            placeholder = "Enter destination city"
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DateRangePicker(
            departureDate = departureDate,
            onDepartureDateSelected = onDepartureDateSelected,
            returnDate = returnDate,
            onReturnDateSelected = onReturnDateSelected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PassengerCountPicker(
            count = passengerCount,
            onCountChange = onPassengerCountChange
        )
    }
} 