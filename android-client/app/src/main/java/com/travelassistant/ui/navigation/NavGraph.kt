package com.travelassistant.ui.navigation

import androidx.compose.material3.Text // For Login placeholder
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
// Import your new RegisterScreen
import com.travelassistant.ui.screens.register.RegisterScreen
// Import your existing screens that will be part of the "main app flow"
import com.travelassistant.ui.screens.MainScreen // Assuming MainScreen hosts your bottom nav + content
// remove other direct screen imports if MainScreen handles them internally

// Hoisted state for SearchScreen is better managed within SearchViewModel or passed differently
// For now, keeping your existing SearchScreen structure for simplicity if MainScreen doesn't isolate it.

@Composable
fun RootNavGraph( // Renamed to RootNavGraph for clarity, MainActivity will call this
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Register.route, // START WITH REGISTER SCREEN
        modifier = modifier
    ) {
        // Authentication Flow
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            // You will implement LoginScreen similar to RegisterScreen
            // For now, a placeholder:
            Text("Login Screen: Implement Me! Navigate to MainAppFlow on success.")
            // Example: LoginScreen(navController = navController)
        }

        // Main Application Flow (after login)
        composable(Screen.MainAppFlow.route) {
            // This is where your app with the bottom navigation bar starts.
            // MainScreen should internally set up its own NavHost for Search, Saved, Notifications, Profile.
            //MainScreen(rootNavController = navController) // Pass rootNavController if MainScreen needs to navigate outside its own flow (e.g. logout)
        }

        // If SearchResults and FlightDetails are part of the MainAppFlow and navigated to
        // from screens within MainScreen's NavHost, they should be defined within
        // MainScreen's NavHost.
        // If they can be reached globally OR if MainScreen doesn't have its own NavHost
        // and this RootNavGraph handles ALL navigation, then they can stay here.
        // For clarity, let's assume for now MainScreen manages its own internal navigation.
        // If not, you'd put your original composable(Screen.Search.route) { ... } etc. here,
        // and MainScreen would just be a Scaffold.

        // Example: If MainScreen DOES NOT have its own NavHost, you would have your
        // original Search, SavedSearches, etc. routes here.
        // For instance:
        // composable(Screen.Search.route) { SearchScreen(...) }
        // composable(Screen.SavedSearches.route) { SavedSearchesScreen(...) }
        // etc.
        // And Screen.MainAppFlow.route would then navigate to Screen.Search.route as default.

        // Given your current NavGraph.kt which defines all these, MainScreen might just be
        // a Scaffold that calls a composable which IS your old NavGraph content.
        // Let's make MainAppFlow lead to a new composable MainAppNavigation which IS your old NavGraph content.

        // THIS IS ONE WAY: MainScreen IS the NavHost for bottom bar items
        // composable(Screen.MainAppFlow.route) {
        //     MainScreen(rootNavController = navController)
        // }

        // ALTERNATIVE: If MainScreen is just a Scaffold and this RootNavGraph handles ALL navigation.
        // In this case, you would NOT navigate to Screen.MainAppFlow.route from Login.
        // Instead, Login would navigate to Screen.Search.route (or your default main screen).
        // And all your original composable routes for Search, SearchResults, Profile, etc.
        // would remain in THIS NavHost.

        // Let's go with the assumption that MainScreen will host the bottom navigation
        // and its own NavHost for the main app screens. This is cleaner for separation.
        // So, from LoginScreen, you will navigate to Screen.MainAppFlow.route.
        // And MainScreen.kt will look something like:

        /*
        // In MainScreen.kt (Conceptual)
        @Composable
        fun MainScreen(rootNavController: NavHostController) {
            val mainAppNavController = rememberNavController()
            Scaffold(
                bottomBar = { BottomNavigationBar(navController = mainAppNavController) }
            ) { paddingValues ->
                AppNavHost( // This is a new NavHost for screens within MainScreen
                    navController = mainAppNavController,
                    modifier = Modifier.padding(paddingValues),
                    rootNavController = rootNavController // Pass if child screens need to trigger root navigation
                )
            }
        }

        @Composable
        fun AppNavHost(navController: NavHostController, modifier: Modifier, rootNavController: NavHostController) {
            NavHost(navController = navController, startDestination = Screen.Search.route, modifier = modifier) {
                composable(Screen.Search.route) { /* Your SearchScreen composable */ }
                composable(Screen.SavedSearches.route) { /* Your SavedSearchesScreen composable */ }
                // ... other main app screens ...
                // SearchResults and FlightDetails would be here if navigated from Search/Saved etc.
                 composable(Screen.SearchResults.route, ...) { SearchResultsScreen(...) }
                 composable(Screen.FlightDetails.route, ...) { FlightDetailsScreen(...) }
            }
        }
        */
    }
}