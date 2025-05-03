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
    object SavedSearches : Screen("saved_searches")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
    
    companion object {
        fun fromRoute(route: String?): Screen {
            return when {
                route?.startsWith("search_results/") == true -> SearchResults
                route == Search.route -> Search
                route == SavedSearches.route -> SavedSearches
                route == Notifications.route -> Notifications
                route == Profile.route -> Profile
                else -> Search // Default to search screen
            }
        }
    }
} 