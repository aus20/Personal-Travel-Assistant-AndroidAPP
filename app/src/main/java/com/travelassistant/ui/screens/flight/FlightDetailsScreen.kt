package com.travelassistant.ui.screens.flight

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.data.AirlineLookup
import com.travelassistant.data.model.searchResults
import com.travelassistant.data.remote.dto.response.FlightLegResponse
import com.travelassistant.ui.components.common.cards.formatToTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
    flight: FlightLegResponse,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = "EEEE, MMMM dd, yyyy"
    val timeFormat = "HH:mm"

    val context = LocalContext.current

    // extract IATA code ("AA" from "AA 1234")
    val code = flight.carrier.split(" ").firstOrNull().orEmpty()

    // remember the lookup per code
    val airlineName = remember(code) {
        AirlineLookup.getName(context, code)
    }

    //val airlines by remember { mutableStateOf(flight.carrier.split(" ").firstOrNull() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$airlineName / ${flight.aircraftCode}",
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
                            text = flight.originAirportCode,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = formatToTime(flight.departureTime, timeFormat),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = formatToTime(flight.departureTime, dateFormat),
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
                            text = flight.destinationAirportCode,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = formatToTime(flight.arrivalTime, timeFormat),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = formatToTime(flight.arrivalTime, dateFormat),
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
                            text = airlineName,
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
                            text = flight.aircraftCode,
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
                            text = when (flight.numberOfStops) {
                                0 -> "Direct"
                                else -> "${flight.numberOfStops} ${if (flight.numberOfStops == 1) "stop" else "stops"}"
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
            flight = searchResults.departureFlight.first(),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Flight Details - With Stops")
@Composable
fun FlightDetailsScreenWithStopsPreview() {
    MaterialTheme {
        FlightDetailsScreen(
            flight = searchResults.departureFlight.first(),
            onBackClick = {}
        )
    }
}