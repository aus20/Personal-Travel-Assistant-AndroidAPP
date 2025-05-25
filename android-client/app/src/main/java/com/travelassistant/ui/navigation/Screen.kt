package com.travelassistant.ui.navigation

/**
 * Sealed class representing all possible screens in the app
 */
sealed class Screen(val route: String) {
    object Search : Screen("search")
    object SearchResults : Screen("search_results/{fromCity}/{toCity}/{departureDate}/{returnDate}") {
        fun createRoute(fromCity: String, toCity: String, departureDate: String, returnDate: String? = null): String {
            return "search_results/$fromCity/$toCity/$departureDate/${returnDate ?: ""}"
        }
    }
    object FlightDetails : Screen("flight_details/{flightId}") {
        fun createRoute(flightId: String): String {
            return "flight_details/$flightId"
        }
    }
    object SavedSearches : Screen("saved_searches")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")

    // New Authentication Screens
    object Register : Screen("register_screen") // <-- ADDED for Register Screen
    object Login : Screen("login_screen")       // <-- ADDED for Login Screen (you'll create this soon)

    object MainAppFlow : Screen("main_app_flow") // New route to represent entering the main app


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