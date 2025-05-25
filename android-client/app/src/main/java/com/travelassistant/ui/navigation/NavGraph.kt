package com.travelassistant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.travelassistant.ui.viewmodel.saved.SavedSearchesViewModel
import com.travelassistant.ui.screens.search.SearchScreen
import com.travelassistant.ui.screens.search.SearchResultsScreen
import com.travelassistant.ui.screens.saved.SavedSearchesScreen
import com.travelassistant.ui.screens.notifications.NotificationsScreen
import com.travelassistant.ui.screens.profile.ProfileScreen
import com.travelassistant.ui.screens.flight.FlightDetailsScreen
import com.travelassistant.data.model.FlightResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Search.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Search.route) {
            // Hoist state for the SearchScreen
            var fromLocation by remember { mutableStateOf("") }
            var toLocation by remember { mutableStateOf("") }
            var departureDate by remember { mutableStateOf<Date?>(null) }
            var returnDate by remember { mutableStateOf<Date?>(null) }
            var passengerCount by remember { mutableStateOf(1) }

            SearchScreen(
                fromLocation = fromLocation,
                toLocation = toLocation,
                departureDate = departureDate,
                returnDate = returnDate,
                passengerCount = passengerCount,
                onFromLocationChange = { fromLocation = it },
                onToLocationChange = { toLocation = it },
                onDepartureDateChange = { departureDate = it },
                onReturnDateChange = { returnDate = it },
                onPassengerCountChange = { passengerCount = it },
                onSearchClick = {
                    // Perform navigation using the hoisted state
                    // Ensure departureDate is not null before formatting,
                    // SearchScreen's button logic should already enforce this.
                    departureDate?.let { depDate ->
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val departureDateStr = dateFormat.format(depDate)
                        val returnDateStr = returnDate?.let { dateFormat.format(it) }

                        navController.navigate(
                            Screen.SearchResults.createRoute(
                                fromCity = fromLocation,
                                toCity = toLocation,
                                departureDate = departureDateStr,
                                returnDate = returnDateStr
                            )
                        )
                    }
                }
            )
        }

            composable(
            route = Screen.SearchResults.route,
            arguments = listOf(
                navArgument("fromCity") { type = NavType.StringType },
                navArgument("toCity") { type = NavType.StringType },
                navArgument("departureDate") { type = NavType.StringType },
                navArgument("returnDate") { type = NavType.StringType; nullable = true; defaultValue = "" }
            )
        ) { backStackEntry ->
            val fromCity = backStackEntry.arguments?.getString("fromCity") ?: ""
            val toCity = backStackEntry.arguments?.getString("toCity") ?: ""
            val departureDateStr = backStackEntry.arguments?.getString("departureDate") ?: ""
            val returnDateStr = backStackEntry.arguments?.getString("returnDate")
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val departureDate = try {
                dateFormat.parse(departureDateStr) ?: Date()
            } catch (e: Exception) {
                Date()
            }
            
            val returnDate = if (!returnDateStr.isNullOrEmpty()) {
                try {
                    dateFormat.parse(returnDateStr)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
            /*
            SearchResultsScreen(
                fromCity = fromCity,
                toCity = toCity,
                departureDate = departureDate,
                returnDate = returnDate,
                onBackClick = { navController.popBackStack() },
                onFlightClick = { flight ->
                    navController.navigate(Screen.FlightDetails.createRoute(flight.id))
                }
            )*/
        }

        composable(
            route = Screen.FlightDetails.route,
            arguments = listOf(
                navArgument("flightId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val flightId = backStackEntry.arguments?.getString("flightId") ?: ""
            // TODO: Fetch flight details using flightId
            // For now, we'll use a dummy flight
            val dummyFlight = FlightResult(
                id = flightId,
                airline = "Delta Airlines",
                airlineLogo = "delta_logo",
                flightNumber = "DL123",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(),
                arrivalTime = Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000),
                duration = "5h 30m",
                stops = 0,
                price = 299.99,
                currency = "USD"
            )
            
            FlightDetailsScreen(
                flight = dummyFlight,
                onBackClick = { navController.popBackStack() }
            )
        }
/*
        composable(Screen.SavedSearches.route) {
            // 1. Obtain ViewModel instance
            val viewModel: SavedSearchesViewModel = hiltViewModel()

            // 2. Collect state from ViewModel
            val savedSearchesList by viewModel.savedSearches.collectAsState()

            // 3. Pass state and event handlers to SavedSearchesScreen
            SavedSearchesScreen(
                savedSearches = savedSearchesList,
                onDeleteSearch = { search -> viewModel.onDeleteSearch(search) }
            )
        }
*/
        composable(Screen.Notifications.route) {
            NotificationsScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
} 