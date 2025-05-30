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
//import com.travelassistant.ui.components.common.inputs.SearchBar
import java.util.Date
//new
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment

@Composable
fun FlightSearchForm(
    fromLocation: String,
    //onOpenFromCityBottomBar: () -> Unit,
    onFromLocationChange: (String) -> Unit,
    toLocation: String,
    //onOpenToCityBottomBar: () -> Unit,
    onToLocationChange: (String) -> Unit,
    departureDate: String? = null,
    onDepartureDateSelected: (Date) -> Unit = {},
    returnDate: String? = null,
    onReturnDateSelected: (Date) -> Unit = {},
    passengerCount: Int = 1,
    onPassengerCountChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        /*
        Text(
            text = "From",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(
            value = fromLocation,
            openCityBottomBar = onOpenFromCityBottomBar,
            placeholder = "Enter departure city"
        )
         */
        OutlinedTextField(
            value = fromLocation,
            onValueChange = onFromLocationChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("From Location") },
            placeholder = { Text("Enter departure city") },
            leadingIcon = { Icon(Icons.Default.FlightTakeoff, contentDescription = "Departure") },
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        /*
        Text(
            text = "To",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(
            value = toLocation,
            openCityBottomBar = onOpenToCityBottomBar,
            placeholder = "Enter destination city"
        )
         */
        OutlinedTextField(
            value = toLocation,
            onValueChange = onToLocationChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("To Location") },
            placeholder = { Text("Enter destination city") },
            leadingIcon = { Icon(Icons.Default.FlightLand, contentDescription = "Arrival") },
            singleLine = true
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
            onCountChange = onPassengerCountChange,
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )
    }
} 