package com.travelassistant.ui.screens

// Layout composables for structuring UI elements
import androidx.compose.foundation.layout.*
// Scrolling functionality
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
// Material Design 3 components
import androidx.compose.material3.*
// State management in Compose
import androidx.compose.runtime.*
// UI modifiers and utilities
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Custom components from our app
import com.travelassistant.ui.components.*
// App theme
import com.travelassistant.ui.theme.TravelAssistantTheme
// Data model
import com.travelassistant.data.model.FlightInfo

/**
 * A test screen that demonstrates all our custom components working together
 * This screen includes:
 * - A search bar for flight searches
 * - A loading state that appears when searching
 * - Sample flight cards showing different routes
 */
@Composable
fun TestScreen() {
    // State management for the screen
    var searchText by remember { mutableStateOf("") }     // Holds search bar text
    var isLoading by remember { mutableStateOf(false) }   // Controls loading state visibility

    TravelAssistantTheme {
        // Main container surface
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Scrollable column containing all components
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())  // Makes content scrollable
                    .padding(16.dp),                        // Adds padding around all content
                verticalArrangement = Arrangement.spacedBy(16.dp)  // Spacing between items
            ) {
                // Search Bar Component
                SearchBar(
                    value = searchText,
                    onValueChange = { searchText = it },
                    hint = "Search for flights",
                    onSearch = { isLoading = true }  // Shows loading state when search is triggered
                )

                // Loading State Component - only visible when isLoading is true
                if (isLoading) {
                    LoadingState(message = "Searching for flights...")
                }

                // Sample Flight Cards
                // First Flight Card - New York to London
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

                // Second Flight Card - San Francisco to Tokyo
                FlightCard(
                    flight = FlightInfo(
                        airline = "Japan Airlines",
                        flightNumber = "JL001",
                        departureTime = "2:30 PM",
                        departureAirport = "SFO - San Francisco",
                        arrivalTime = "6:30 PM",
                        arrivalAirport = "HND - Tokyo",
                        price = 899.99
                    )
                )
            }
        }
    }
} 