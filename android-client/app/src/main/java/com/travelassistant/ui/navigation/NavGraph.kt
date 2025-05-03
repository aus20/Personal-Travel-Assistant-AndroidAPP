package com.travelassistant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.travelassistant.ui.screens.search.SearchScreen
import com.travelassistant.ui.screens.search.SearchResultsScreen
import com.travelassistant.ui.screens.saved.SavedSearchesScreen
import com.travelassistant.ui.screens.notifications.NotificationsScreen
import com.travelassistant.ui.screens.profile.ProfileScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
            SearchScreen(
                onSearchResults = { fromCity, toCity, departureDate, returnDate ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val departureDateStr = dateFormat.format(departureDate)
                    val returnDateStr = returnDate?.let { dateFormat.format(it) }
                    
                    navController.navigate(
                        Screen.SearchResults.createRoute(
                            fromCity = fromCity,
                            toCity = toCity,
                            departureDate = departureDateStr,
                            returnDate = returnDateStr
                        )
                    )
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
            
            SearchResultsScreen(
                fromCity = fromCity,
                toCity = toCity,
                departureDate = departureDate,
                returnDate = returnDate,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SavedSearches.route) {
            SavedSearchesScreen()
        }

        composable(Screen.Notifications.route) {
            NotificationsScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
} 