package com.travelassistant

// Android core components
import android.os.Bundle
// Base activity class for Compose
import androidx.activity.ComponentActivity
// Compose integration with Activity
import androidx.activity.compose.setContent
// Our test screen component
import com.travelassistant.ui.screens.TestScreen
// App theme
import com.travelassistant.ui.theme.TravelAssistantTheme

/**
 * Main entry point of the application
 * This activity sets up the Compose UI and displays our test screen
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the Compose UI content
        setContent {
            // Apply our custom theme to the entire app
            TravelAssistantTheme {
                // Display the test screen with all our components
                TestScreen()
            }
        }
    }
} 