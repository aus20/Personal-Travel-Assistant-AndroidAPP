package com.travelassistant.ui.navigation

import android.net.Uri
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.local.entity.SearchDetails
import com.travelassistant.data.remote.dto.response.FlightLegResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Sealed class representing all possible screens in the app
 */
sealed class Screen(val route: String) {
    object Search : Screen("search") {
        const val BASE_ROUTE = "search"

        fun createRoute(
            from: String? = null,
            to: String? = null,
            departure: String? = null,
            returnDate: String? = null,
            passengerCount: Int? = null
        ): String {
            val params = listOfNotNull(
                from?.let { "from=${Uri.encode(it)}" },
                to?.let { "to=${Uri.encode(it)}" },
                departure?.let { "departure=${Uri.encode(it)}" },
                returnDate?.let { "returnDate=${Uri.encode(it)}" },
                passengerCount?.let { "passengerCount=$it" }
            ).joinToString("&")

            return if (params.isNotEmpty()) {
                "$BASE_ROUTE?$params"
            } else {
                BASE_ROUTE
            }
        }
    }



    object SearchResults : Screen("search_results") {
        fun createRoute(details: SearchDetails): String {
            val encoded = Uri.encode(Json.encodeToString(details))
            return "search_results/$encoded"
        }
    }


    object FlightDetails : Screen("flight_details") {
        fun createRoute(flight: FlightLegResponse): String {
            val encoded = Uri.encode(Json.encodeToString(flight))
            return "flight_details?data=$encoded"
        }
    }

    object SavedSearches : Screen("saved_searches")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")

    // New Authentication Screens
    object Register : Screen("register_screen") // <-- ADDED for Register Screen
    object Login : Screen("login_screen")       // <-- ADDED for Login Screen

    // You might want a Main screen route if you have a BottomNavigationBar managing Search, SavedSearches, etc.
    // If your MainScreen composable (with bottom nav) is a distinct destination:
    // object MainApp : Screen("main_app_flow") // Example if MainScreen hosts other top-level destinations

    companion object {
        fun fromRoute(route: String?): Screen {
            return when {
                route?.startsWith("search_results/") == true -> SearchResults
                route?.startsWith("flight_details/") == true -> FlightDetails
                route == Search.route -> Search
                route == SavedSearches.route -> SavedSearches
                route == Notifications.route -> Notifications
                route == Profile.route -> Profile
                route == Register.route -> Register // <-- ADDED
                route == Login.route -> Login       // <-- ADDED
                // route == MainApp.route -> MainApp // Example
                else -> Search // Default to search screen, or perhaps Login if auth is mandatory
            }
        }
    }
}


fun SearchDetails.toQuery(): String {
    return listOfNotNull(
        "userId=${Uri.encode(userId)}",
        "origin=${Uri.encode(origin)}",
        "destination=${Uri.encode(destination)}",
        "departureDate=${Uri.encode(departureDate)}",
        returnDate?.let { "returnDate=${Uri.encode(it)}" },
        "maxPrice=$maxPrice",
        "passengers=$passengers"
    ).joinToString("&")
}