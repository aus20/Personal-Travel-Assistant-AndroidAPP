package com.travelassistant.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.travelassistant.data.model.FlightResult
import com.travelassistant.ui.components.common.cards.DetailedFlightCard
import com.travelassistant.ui.components.common.states.EmptyState
import com.travelassistant.ui.components.features.search.FilterSheet
import com.travelassistant.ui.components.features.search.FlightFilters
import java.util.Date
import androidx.compose.ui.tooling.preview.Preview

// Sort options for flight results
enum class SortOption {
    PRICE_LOW_TO_HIGH,
    PRICE_HIGH_TO_LOW,
    DURATION_SHORT_TO_LONG,
    DEPARTURE_TIME_EARLY_TO_LATE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsScreen(
    fromCity: String,
    toCity: String,
    departureDate: Date,
    returnDate: Date? = null,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sample flight results data
    val allFlights = remember {
        listOf(
            FlightResult(
                id = "1",
                airline = "Delta Airlines",
                airlineLogo = "delta_logo",
                flightNumber = "1",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(),
                arrivalTime = Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000),
                duration = "5h 30m",
                stops = 0,
                price = 299.99
            ),
            FlightResult(
                id = "2",
                airline = "United Airlines",
                airlineLogo = "united_logo",
                flightNumber = "2",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000),
                arrivalTime = Date(System.currentTimeMillis() + 7 * 60 * 60 * 1000),
                duration = "5h 00m",
                stops = 1,
                price = 245.50
            ),
            FlightResult(
                id = "3",
                airline = "American Airlines",
                airlineLogo = "american_logo",
                flightNumber = "3",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(System.currentTimeMillis() + 4 * 60 * 60 * 1000),
                arrivalTime = Date(System.currentTimeMillis() + 9 * 60 * 60 * 1000),
                duration = "5h 00m",
                stops = 0,
                price = 325.75
            ),
            FlightResult(
                id = "4",
                airline = "JetBlue",
                airlineLogo = "jetblue_logo",
                flightNumber = "4",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(System.currentTimeMillis() + 6 * 60 * 60 * 1000),
                arrivalTime = Date(System.currentTimeMillis() + 12 * 60 * 60 * 1000),
                duration = "6h 00m",
                stops = 1,
                price = 275.25
            ),
            FlightResult(
                id = "5",
                airline = "Southwest Airlines",
                airlineLogo = "southwest_logo",
                flightNumber = "5",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(System.currentTimeMillis() + 8 * 60 * 60 * 1000),
                arrivalTime = Date(System.currentTimeMillis() + 14 * 60 * 60 * 1000),
                duration = "6h 00m",
                stops = 2,
                price = 225.00
            )
        )
    }
    
    // State for sort dropdown
    var showSortMenu by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf(SortOption.PRICE_LOW_TO_HIGH) }
    
    // State for filter sheet
    var showFilterSheet by remember { mutableStateOf(false) }
    
    // State for loading
    var isLoading by remember { mutableStateOf(false) }
    
    // State for filters
    var currentFilters by remember { mutableStateOf(FlightFilters()) }
    
    // Get available airlines from all flights
    val availableAirlines = remember(allFlights) {
        allFlights.map { it.airline }.distinct()
    }
    
    // Apply filters and sorting to flights
    val filteredAndSortedFlights = remember(allFlights, currentFilters, selectedSortOption) {
        var result = allFlights.filter { flight ->
            // Apply price filter
            val priceInRange = flight.price >= currentFilters.priceRange.start && 
                              flight.price <= currentFilters.priceRange.endInclusive
            
            // Apply stops filter
            val stopsInRange = flight.stops <= currentFilters.maxStops
            
            // Apply airline filter
            val airlineSelected = currentFilters.selectedAirlines.isEmpty() || 
                                currentFilters.selectedAirlines.contains(flight.airline)
            
            priceInRange && stopsInRange && airlineSelected
        }
        
        // Apply sorting
        result = when (selectedSortOption) {
            SortOption.PRICE_LOW_TO_HIGH -> result.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> result.sortedByDescending { it.price }
            SortOption.DURATION_SHORT_TO_LONG -> result.sortedBy { 
                it.duration.replace("h", "").replace("m", "").toIntOrNull() ?: 0 
            }
            SortOption.DEPARTURE_TIME_EARLY_TO_LATE -> result.sortedBy { it.departureTime }
        }
        
        result
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "$fromCity → $toCity",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Departure: ${departureDate.toString().substring(0, 10)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Sort button
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }
                    
                    // Filter button
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                    
                    // Sort dropdown menu
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Price: Low to High") },
                            onClick = {
                                selectedSortOption = SortOption.PRICE_LOW_TO_HIGH
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Price: High to Low") },
                            onClick = {
                                selectedSortOption = SortOption.PRICE_HIGH_TO_LOW
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Duration: Short to Long") },
                            onClick = {
                                selectedSortOption = SortOption.DURATION_SHORT_TO_LONG
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Departure: Early to Late") },
                            onClick = {
                                selectedSortOption = SortOption.DEPARTURE_TIME_EARLY_TO_LATE
                                showSortMenu = false
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            // Show loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                // TODO: Add loading indicator
                Text("Loading flights...")
            }
        } else if (filteredAndSortedFlights.isEmpty()) {
            // Show empty state
            EmptyState(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = "No flights found",
                description = "Try adjusting your search criteria",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            // Show flight results
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    // Results count
                    Text(
                        text = "${filteredAndSortedFlights.size} flights found",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Sort indicator
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 0.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (selectedSortOption) {
                                    SortOption.PRICE_LOW_TO_HIGH -> "Price: Low to High"
                                    SortOption.PRICE_HIGH_TO_LOW -> "Price: High to Low"
                                    SortOption.DURATION_SHORT_TO_LONG -> "Duration: Short to Long"
                                    SortOption.DEPARTURE_TIME_EARLY_TO_LATE -> "Departure: Early to Late"
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(bottom = 16.dp))
                }
                
                // Flight results
                items(filteredAndSortedFlights) { flight ->
                    DetailedFlightCard(
                        airline = flight.airline,
                        airlineLogo = flight.airlineLogo,
                        flightNumber = flight.id,
                        departureAirport = flight.departureAirport,
                        arrivalAirport = flight.arrivalAirport,
                        departureTime = flight.departureTime,
                        arrivalTime = flight.arrivalTime,
                        duration = flight.duration,
                        stops = flight.stops,
                        price = flight.price,
                        currency = flight.currency,
                        onClick = { /* TODO: Navigate to flight details */ }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        
        // Filter sheet
        if (showFilterSheet) {
            FilterSheet(
                availableAirlines = availableAirlines,
                currentFilters = currentFilters,
                onDismiss = { showFilterSheet = false },
                onApplyFilters = { newFilters ->
                    currentFilters = newFilters
                },
                onResetFilters = {
                    currentFilters = FlightFilters()
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Search Results - With Flights")
@Composable
fun SearchResultsScreenPreview() {
    MaterialTheme {
        SearchResultsScreen(
            fromCity = "New York",
            toCity = "London",
            departureDate = Date(),
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Search Results - Empty")
@Composable
fun SearchResultsScreenEmptyPreview() {
    MaterialTheme {
        SearchResultsScreen(
            fromCity = "Paris",
            toCity = "Tokyo",
            departureDate = Date(),
            onBackClick = {}
        )
    }
} 