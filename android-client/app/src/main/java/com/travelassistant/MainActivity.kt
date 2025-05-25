package com.travelassistant

// Android core components
import android.os.Bundle
// Base activity class for Compose
import androidx.activity.ComponentActivity
// Compose integration with Activity
import androidx.activity.compose.setContent
// Our test screen component
import com.travelassistant.ui.screens.MainScreen
// App theme
import com.travelassistant.ui.theme.TravelAssistantTheme

import androidx.navigation.compose.rememberNavController
import com.travelassistant.ui.navigation.RootNavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application
 * This activity sets up the Compose UI and displays our test screen
 */



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelAssistantTheme {
                val navController = rememberNavController()
                RootNavGraph(navController = navController) // Call your root graph
            }
        }
    }
}

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the Compose UI content
        setContent {
            // Apply our custom theme to the entire app
            TravelAssistantTheme {
                // Display the test screen with all our components
                MainScreen()
            }
        }
    }
} */