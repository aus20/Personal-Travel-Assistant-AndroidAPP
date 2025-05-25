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

import androidx.navigation.compose.rememberNavController
import com.travelassistant.ui.navigation.RootNavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main entry point of the application
 * This activity sets up the Compose UI and displays our test screen
 */



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  // private val authViewModel: AuthViewModel by viewModels() // AuthViewModel'i Hilt ile al
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
         // ------ TEST AMAÇLI ÇAĞRI ------
        Log.d("MainActivityTest", "onCreate çağrıldı, testRegisterUser tetikleniyor.")
        authViewModel.testLoginUser() 
        // ------ TEST AMAÇLI ÇAĞRI SONU -----*/
        setContent {
            TravelAssistantTheme {
                val navController = rememberNavController()
                RootNavGraph(navController = navController) // Call your root graph
            }
        }
    }
}
