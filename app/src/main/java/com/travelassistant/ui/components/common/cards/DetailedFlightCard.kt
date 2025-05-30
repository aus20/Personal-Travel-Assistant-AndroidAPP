package com.travelassistant.ui.components.common.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.data.remote.dto.response.FlightLegResponse
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedFlightCard(
    flight: FlightLegResponse,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timeFormat = "HH:mm"
    val dateFormat = "MMM dd, yyyy"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Airline and Flight Number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = flight.carrier,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = flight.aircraftCode,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = "Departure",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = flight.originAirportCode,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = formatToTime(flight.departureTime, convertFormat = timeFormat),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = formatToTime(flight.departureTime, convertFormat = dateFormat),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Flight Icon
                Icon(
                    imageVector = Icons.Default.AirplanemodeActive,
                    contentDescription = "Flight",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Arrival
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = flight.destinationAirportCode,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.FlightLand,
                            contentDescription = "Arrival",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = formatToTime(flight.arrivalTime, convertFormat = timeFormat),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = formatToTime(flight.arrivalTime, convertFormat = dateFormat),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Duration
                Column {
                    Text(
                        text = "Duration",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = flight.duration,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Stops
                Column {
                    Text(
                        text = "Stops",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = when (flight.numberOfStops ) {
                            0 -> "Direct"
                            else -> "${flight.numberOfStops} ${if (flight.numberOfStops == 1) "stop" else "stops"}"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Price
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${flight.currency} ${String.format("%.2f", flight.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true, name = "Search Results - Empty")
@Composable
fun DetailedFlightCardPreview() {
    MaterialTheme {
        val flight = FlightLegResponse(
            origin = "Istanbul",
            destination = "London",
            originAirportCode = "ISTM",
            destinationAirportCode = "LONK",
            layoverAirports = emptyList(),
            departureTime = "2025-07-15T14:47:00",
            arrivalTime = "2025-07-15T19:04:00",
            carrier = "TK 4301",
            duration = "4H17M",
            aircraftCode = "321",
            cabinClass = "ECONOMY",
            numberOfStops = 0,
            price = 98.77007561688791,
            currency = "USD",
            leg = "DEPARTURE"
        )

        DetailedFlightCard(
            flight = flight,
            onClick = {}
        )
    }
}

fun formatToTime(input: String, convertFormat : String): String {
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC") // Zaman dilimini ihtiyaç durumuna göre ayarla

        val date = isoFormat.parse(input)
        val timeFormat = SimpleDateFormat(convertFormat, Locale.getDefault())

        date?.let { timeFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}
