package com.travelassistant

// Android core components
import android.os.Bundle
import android.util.Log
// Base activity class for Compose
import androidx.activity.ComponentActivity
// Compose integration with Activity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
// Our test screen component
import com.travelassistant.ui.screens.MainScreen
// App theme
import com.travelassistant.ui.theme.TravelAssistantTheme
import com.travelassistant.ui.viewmodel.authTest.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application
 * This activity sets up the Compose UI and displays our test screen
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Test için yapıldı
    private val authViewModel: AuthViewModel by viewModels() // AuthViewModel'i Hilt ile al

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ------ TEST AMAÇLI ÇAĞRI ------
        Log.d("MainActivityTest", "onCreate çağrıldı, testRegisterUser tetikleniyor.")
        authViewModel.testLoginUser() 
        // ------ TEST AMAÇLI ÇAĞRI SONU ------

        // Set up the Compose UI content
        setContent {
            // Apply our custom theme to the entire app
            TravelAssistantTheme {
                // Display the test screen with all our components
                MainScreen()
            }
        }
    }
} 