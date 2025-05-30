package com.travelassistant.ui.screens.search

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.cards.DetailedFlightCard
import com.travelassistant.ui.components.common.states.EmptyState
import com.travelassistant.ui.components.features.search.FilterSheet
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.travelassistant.data.local.entity.SearchDetails
import com.travelassistant.data.remote.dto.response.FlightLegResponse
import com.travelassistant.ui.components.features.search.FlightFilters
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.search.SearchResultViewModel
import com.travelassistant.ui.viewmodel.search.SearchResultsState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    searchDetails: SearchDetails,
    onBackClick: () -> Unit,
    onFlightClick: (FlightLegResponse) -> Unit,
    viewModel: SearchResultViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val searchResultsState by viewModel.searchResultsState.collectAsState(SearchResultsState.Initial)
    val savedSearchStatus : UiState<Boolean> by viewModel.savedSearchStatus.collectAsState(UiState.Initial)

    LaunchedEffect(Unit) {
        viewModel.getSearchedFlights(searchDetails = searchDetails)
    }

    LaunchedEffect(savedSearchStatus) {
        when (savedSearchStatus) {
            is UiState.Success<Boolean> -> {
                val message = if ((savedSearchStatus as UiState.Success<Boolean>).data) "Saved"
                else "Failed"
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
            else-> {}
        }
    }


    var maxFlightPrice by remember { mutableFloatStateOf(1000.0f) }


    var showSortMenu by remember { mutableStateOf(false) }
    var showFilterBottomSheet by remember { mutableStateOf(false) }

    var onSortOptionSelected by remember { mutableStateOf(SortOption.DEPARTURE_TIME_EARLY_TO_LATE) }



    // Local state for filters
    var priceRange by remember { mutableStateOf(0.0f..1000.0f) }
    var maxStops by remember { mutableIntStateOf(2) }
    var selectedAirlines by remember { mutableStateOf(listOf<String>()) }


    LaunchedEffect(maxFlightPrice) {
        val newMax = maxFlightPrice
        maxFlightPrice = newMax
        priceRange = priceRange.start ..newMax
    }


   // val state = viewModel._state.collectAsState(UiState.Success<SearchResultsState>())
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    searchDetails?.let {
                        Column {
                            Text(
                                text = "${it.origin} → ${it.destination}",
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Departure: ${it.departureDate}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
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
                    IconButton(onClick = {
                        viewModel.saveSearch(
                            searchDetails = searchDetails,
                            maxStops = maxStops,
                            priceRange = priceRange
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Button"
                        )
                    }

                    // Sort button
                    IconButton(onClick = {
                        showSortMenu = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Sort"
                        )
                    }

                    // Filter button
                    IconButton(onClick = {
                        showFilterBottomSheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                    
                    // Sort dropdown menu
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = {
                            showSortMenu = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Price: Low to High") },
                            onClick = {
                                onSortOptionSelected = SortOption.PRICE_LOW_TO_HIGH
                                showSortMenu = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = if (onSortOptionSelected == SortOption.PRICE_LOW_TO_HIGH) Color.Red else Color.Unspecified
                            )
                        )
                        DropdownMenuItem(
                            text = { Text("Price: High to Low") },
                            onClick = {
                                onSortOptionSelected = SortOption.PRICE_HIGH_TO_LOW
                                showSortMenu = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = if (onSortOptionSelected == SortOption.PRICE_HIGH_TO_LOW) Color.Red else Color.Black
                            )
                        )
                        DropdownMenuItem(
                            text = { Text("Duration: Short to Long") },
                            onClick = {
                                onSortOptionSelected = SortOption.DURATION_SHORT_TO_LONG
                                showSortMenu = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = if (onSortOptionSelected == SortOption.DURATION_SHORT_TO_LONG) Color.Red else Color.Unspecified
                            )
                        )
                        DropdownMenuItem(
                            text = { Text("Departure: Early to Late") },
                            onClick = {
                                onSortOptionSelected = SortOption.DEPARTURE_TIME_EARLY_TO_LATE
                                showSortMenu = false
                            },
                            colors = MenuDefaults.itemColors(
                                textColor = if (onSortOptionSelected == SortOption.DEPARTURE_TIME_EARLY_TO_LATE) Color.Red else Color.Unspecified
                            )
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
           // if (!isOnline) {
          //      OfflineStatusBar()
          //  }
            
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when(searchResultsState){
                    is SearchResultsState.Error -> {

                        // Show empty state
                        EmptyState(
                            title = "No Flights Found",
                            description = "Try adjusting your filters or search criteria",
                            icon = { Icons.Default.Search },
                        )
                    }
                    SearchResultsState.Initial -> {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading flights...")
                        }
                    }
                    SearchResultsState.Loading -> {
                        // Show loading state
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Loading flights...")
                        }
                    }
                    is SearchResultsState.Success -> {
                        val searchedFlights = (searchResultsState as SearchResultsState.Success).flights
                        maxFlightPrice = searchedFlights.departureFlight.maxOf { it.price }.toFloat()

                        val sortedFlights =
                            sortAndFilterFlights(
                                flights = searchedFlights.departureFlight,
                                sortOption = onSortOptionSelected,
                                priceRange = priceRange,
                                maxStops = maxStops,
                                selectedAirlines = selectedAirlines,
                            )
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(sortedFlights) { flight ->
                                DetailedFlightCard(
                                    flight = flight,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    onClick = {
                                        onFlightClick.invoke(flight)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                when(savedSearchStatus){
                    UiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    else -> {

                    }
                }
            }

        }
        
        // Filter sheet
        if (showFilterBottomSheet) {
            FilterSheet(
                availableAirlines = listOf(),
                currentFilters = FlightFilters(
                    priceRange = priceRange,
                    maxStops = maxStops,
                    selectedAirlines = selectedAirlines,
                ),
                onDismiss = {
                    showFilterBottomSheet = false
                },
                onApplyFilters = {
                    priceRange = it.priceRange
                    maxStops = it.maxStops
                    selectedAirlines = it.selectedAirlines
                },
                onResetFilters = {

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
            searchDetails = SearchDetails("", "", "", "", "", 0.0, 1),
            onBackClick = {},
            onFlightClick = {},
        )
    }
}


fun sortAndFilterFlights(
    flights: List<FlightLegResponse>,
    sortOption: SortOption,
    priceRange: ClosedFloatingPointRange<Float>,
    maxStops: Int,
    selectedAirlines: List<String>
): List<FlightLegResponse> {
    val filteredFlights = flights.filter { flight ->
        (priceRange == FlightFilters().priceRange || flight.price.toFloat() in priceRange) &&
                (flight.numberOfStops <= maxStops) &&
                (selectedAirlines.isEmpty() || selectedAirlines.contains(flight.carrier))
    }

    return when (sortOption) {
        SortOption.PRICE_LOW_TO_HIGH -> filteredFlights.sortedBy { it.price }
        SortOption.PRICE_HIGH_TO_LOW -> filteredFlights.sortedByDescending { it.price }
        SortOption.DURATION_SHORT_TO_LONG -> filteredFlights.sortedBy { parseDuration(it.duration) }
        SortOption.DEPARTURE_TIME_EARLY_TO_LATE -> filteredFlights.sortedBy { parseDate(it.departureTime) }
    }
}



fun parseDate(dateString: String): Date? {
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        isoFormat.parse(dateString)
    } catch (e: Exception) {
        null
    }
}


fun parseDuration(duration: String): Int {
    /*
    val hourRegex = Regex("(\\d+)H")
    val minuteRegex = Regex("(\\d+)M")

    val hours = hourRegex.find(duration)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    val minutes = minuteRegex.find(duration)?.groupValues?.get(1)?.toIntOrNull() ?: 0

    return hours * 60 + minutes

     */
    // Example input: "15 hours 10 minutes"
    // Regex to find "X hours" - matches digits followed by optional space and "hour" or "hours"
    val hourRegex = Regex("(\\d+)\\s*hours?")
    // Regex to find "Y minutes" - matches digits followed by optional space and "minute" or "minutes"
    val minuteRegex = Regex("(\\d+)\\s*minutes?")

    var totalMinutes = 0

    // Find and add hours to totalMinutes
    hourRegex.find(duration)?.groupValues?.get(1)?.toIntOrNull()?.let { hours ->
        totalMinutes += hours * 60
    }

    // Find and add minutes to totalMinutes
    minuteRegex.find(duration)?.groupValues?.get(1)?.toIntOrNull()?.let { minutes ->
        totalMinutes += minutes
    }

    // Handle cases where only hours or only minutes might be present,
    // or if the string doesn't match either (though less likely for a consistent API).
    // For example, "2 hours" should parse to 120. "30 minutes" should parse to 30.

    return totalMinutes
}
