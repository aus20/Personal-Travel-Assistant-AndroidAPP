package com.travelassistant.ui.components

// Android Configuration for handling different UI modes (e.g., dark/light theme)
import android.content.res.Configuration
// Layout composables for structuring UI elements
import androidx.compose.foundation.layout.*
// Scrolling functionality
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// Material Icons for flight-related icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
// Material Design 3 components and theming
import androidx.compose.material3.*
// Core Compose annotations and components
import androidx.compose.runtime.Composable
// UI alignment and positioning utilities
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Text styling utilities
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
// Preview annotation for Android Studio preview
import androidx.compose.ui.tooling.preview.Preview
// Dimension utilities for spacing and sizing
import androidx.compose.ui.unit.dp
// Custom theme for consistent styling
import com.travelassistant.ui.theme.TravelAssistantTheme
import com.travelassistant.data.model.FlightInfo

/**
 * A composable that displays a flight card with departure and arrival information
 * This card shows:
 * - Airline name and flight number
 * - Departure and arrival airports with times
 * - Price information
 */
@Composable
fun FlightCard(
    flight: FlightInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Airline and Flight Number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = flight.airline,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = flight.flightNumber,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Departure
                Column {
                    Icon(
                        imageVector = Icons.Default.FlightTakeoff,
                        contentDescription = "Departure",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = flight.departureAirport,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = flight.departureTime,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Flight Icon
                Icon(
                    imageVector = Icons.Default.Flight,
                    contentDescription = "Flight",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                // Arrival
                Column {
                    Icon(
                        imageVector = Icons.Default.FlightLand,
                        contentDescription = "Arrival",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = flight.arrivalAirport,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = flight.arrivalTime,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price
            Text(
                text = "$${flight.price}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Preview composable for FlightCard in both light and dark themes
 * Shows a sample flight card with mock data
 */
@Preview(
    name = "Flight Card Light",
    showBackground = true
)
@Preview(
    name = "Flight Card Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FlightCardPreview() {
    TravelAssistantTheme {
        Surface {
            FlightCard(
                flight = FlightInfo(
                    airline = "British Airways",
                    flightNumber = "BA123",
                    departureTime = "10:00 AM",
                    departureAirport = "JFK - New York",
                    arrivalTime = "10:00 PM",
                    arrivalAirport = "LHR - London",
                    price = 599.99
                )
            )
        }
    }
} 