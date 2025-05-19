package com.travelassistant.ui.screens.flight

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.data.model.FlightResult
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
    flight: FlightResult,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${flight.airline} ${flight.flightNumber}",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Flight Route Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Flight Route",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Departure
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = flight.departureAirport,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = timeFormat.format(flight.departureTime),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = dateFormat.format(flight.departureTime),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Duration
                    Text(
                        text = flight.duration,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Arrival
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = flight.arrivalAirport,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = timeFormat.format(flight.arrivalTime),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = dateFormat.format(flight.arrivalTime),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Flight Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Flight Details",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Airline
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Airline",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = flight.airline,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Flight Number
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Flight Number",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = flight.flightNumber,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Stops
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stops",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = when (flight.stops) {
                                0 -> "Direct"
                                else -> "${flight.stops} ${if (flight.stops == 1) "stop" else "stops"}"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Price Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${flight.currency} ${String.format("%.2f", flight.price)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, name = "Flight Details")
@Composable
fun FlightDetailsScreenPreview() {
    MaterialTheme {
        FlightDetailsScreen(
            flight = FlightResult(
                id = "1",
                airline = "Turkish Airlines",
                airlineLogo = "delta_logo",
                flightNumber = "TA5378",
                departureAirport = "IST",
                arrivalAirport = "CDG",
                departureTime = Date(),
                arrivalTime = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000),
                duration = "3h",
                stops = 0,
                price = 189.99,
                currency = "USD"
            ),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Flight Details - With Stops")
@Composable
fun FlightDetailsScreenWithStopsPreview() {
    MaterialTheme {
        FlightDetailsScreen(
            flight = FlightResult(
                id = "2",
                airline = "United Airlines",
                airlineLogo = "united_logo",
                flightNumber = "UA456",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000),
                arrivalTime = Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000),
                duration = "6h 15m",
                stops = 1,
                price = 245.50,
                currency = "USD"
            ),
            onBackClick = {}
        )
    }
} 