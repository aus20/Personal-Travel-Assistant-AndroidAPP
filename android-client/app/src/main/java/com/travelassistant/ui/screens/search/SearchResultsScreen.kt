package com.travelassistant.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.travelassistant.data.model.FlightResult
import com.travelassistant.ui.components.common.cards.DetailedFlightCard
import com.travelassistant.ui.components.common.states.EmptyState
import com.travelassistant.ui.components.common.states.OfflineStatusBar
import com.travelassistant.ui.components.features.search.FilterSheet
import com.travelassistant.ui.components.features.search.FlightFilters
import java.util.Date
import java.util.Calendar
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
    flights: List<FlightResult>,
    isLoading: Boolean,
    isOnline: Boolean,
    currentFilters: FlightFilters,
    selectedSortOption: SortOption,
    showSortMenu: Boolean,
    showFilterSheet: Boolean,
    availableAirlines: List<String>,
    onBackClick: () -> Unit,
    onFlightClick: (FlightResult) -> Unit,
    onSortOptionSelected: (SortOption) -> Unit,
    onSortMenuVisibilityChanged: (Boolean) -> Unit,
    onFilterSheetVisibilityChanged: (Boolean) -> Unit,
    onFiltersApplied: (FlightFilters) -> Unit,
    onFiltersReset: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    IconButton(onClick = { onSortMenuVisibilityChanged(true) }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }
                    
                    // Filter button
                    IconButton(onClick = { onFilterSheetVisibilityChanged(true) }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                    
                    // Sort dropdown menu
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { onSortMenuVisibilityChanged(false) }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Price: Low to High") },
                            onClick = {
                                onSortOptionSelected(SortOption.PRICE_LOW_TO_HIGH)
                                onSortMenuVisibilityChanged(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Price: High to Low") },
                            onClick = {
                                onSortOptionSelected(SortOption.PRICE_HIGH_TO_LOW)
                                onSortMenuVisibilityChanged(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Duration: Short to Long") },
                            onClick = {
                                onSortOptionSelected(SortOption.DURATION_SHORT_TO_LONG)
                                onSortMenuVisibilityChanged(false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Departure: Early to Late") },
                            onClick = {
                                onSortOptionSelected(SortOption.DEPARTURE_TIME_EARLY_TO_LATE)
                                onSortMenuVisibilityChanged(false)
                            }
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
        ) {
            // Show offline status bar when offline
            if (!isOnline) {
                OfflineStatusBar()
            }
            
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (isLoading) {
                    // Show loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Loading flights...")
                    }
                } else if (flights.isEmpty()) {
                    // Show empty state
                    EmptyState(
                        title = "No Flights Found",
                        description = "Try adjusting your filters or search criteria",
                        icon = { Icons.Default.Search },
                    )
                } else {
                    // Show flight results
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(flights) { flight ->
                            DetailedFlightCard(
                                flight = flight,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                onClick = { onFlightClick(flight) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        
        // Filter sheet
        if (showFilterSheet) {
            FilterSheet(
                availableAirlines = availableAirlines,
                currentFilters = currentFilters,
                onDismiss = { onFilterSheetVisibilityChanged(false) },
                onApplyFilters = onFiltersApplied,
                onResetFilters = onFiltersReset
            )
        }
    }
}

@Preview(showBackground = true, name = "Search Results - With Flights")
@Composable
fun SearchResultsScreenPreview() {

    fun createDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // Calendar.MONTH is 0-indexed
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    val mockFlights = listOf(
        FlightResult(
            id = "1",
            airline = "Turkish Airlines",
            airlineLogo = "https://via.placeholder.com/50?text=SA", // Placeholder logo URL
            flightNumber = "TK5343",
            departureAirport = "IST",
            arrivalAirport = "LHR",
            departureTime = createDate(2025, 6, 1, 10, 30),
            arrivalTime = createDate(2025, 6, 1, 11, 45),
            duration = "3h 15m",
            stops = 0,
            price = 125.99,
            currency = "USD"
        ),
        FlightResult(
            id = "2",
            airline = "Turkish Airlines",
            airlineLogo = "https://via.placeholder.com/50?text=BS", // Placeholder logo URL
            flightNumber = "TK5362",
            departureAirport = "IST",
            arrivalAirport = "LHR",
            departureTime = createDate(2025, 6, 1, 12, 0),
            arrivalTime = createDate(2025, 6, 1, 13, 30), // Longer flight with a stop
            duration = "3h 30m",
            stops = 0,
            price = 130.79,
            currency = "USD"
        ),
        FlightResult(
            id = "3",
            airline = "Turkish Airlines",
            airlineLogo = "https://via.placeholder.com/50?text=CF", // Placeholder logo URL
            flightNumber = "TK5381",
            departureAirport = "IST",
            arrivalAirport = "LHR",
            departureTime = createDate(2025, 6, 1, 17, 0),
            arrivalTime = createDate(2025, 6, 1, 18, 30),
            duration = "3h 30m",
            stops = 0,
            price = 114.92,
            currency = "USD"
        )
    )

    MaterialTheme {
        SearchResultsScreen(
            fromCity = "Istanbul",
            toCity = "London",
            departureDate = createDate(2025, 6, 1, 0, 0),
            flights = mockFlights,
            isLoading = false,
            isOnline = true,
            currentFilters = FlightFilters(),
            selectedSortOption = SortOption.PRICE_LOW_TO_HIGH,
            showSortMenu = false,
            showFilterSheet = false,
            availableAirlines = listOf("Star Airline", "BlueSky Airways", "Connect Flights"),
            onBackClick = {},
            onFlightClick = {},
            onSortOptionSelected = {},
            onSortMenuVisibilityChanged = {},
            onFilterSheetVisibilityChanged = {},
            onFiltersApplied = {},
            onFiltersReset = {}
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
            flights = emptyList(),
            isLoading = false,
            isOnline = true,
            currentFilters = FlightFilters(),
            selectedSortOption = SortOption.PRICE_LOW_TO_HIGH,
            showSortMenu = false,
            showFilterSheet = false,
            availableAirlines = emptyList(),
            onBackClick = {},
            onFlightClick = {},
            onSortOptionSelected = {},
            onSortMenuVisibilityChanged = {},
            onFilterSheetVisibilityChanged = {},
            onFiltersApplied = {},
            onFiltersReset = {}
        )
    }
} 