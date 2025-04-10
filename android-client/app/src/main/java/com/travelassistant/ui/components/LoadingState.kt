package com.travelassistant.ui.components

// Android Configuration for handling different UI modes (e.g., dark/light theme)
import android.content.res.Configuration
// Layout composables for structuring UI elements
import androidx.compose.foundation.layout.*
// Material Design 3 components and theming
import androidx.compose.material3.*
// Core Compose annotations and components
import androidx.compose.runtime.Composable
// UI alignment and positioning utilities
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Text styling utilities
import androidx.compose.ui.text.style.TextAlign
// Preview annotation for Android Studio preview
import androidx.compose.ui.tooling.preview.Preview
// Dimension utilities for spacing and sizing
import androidx.compose.ui.unit.dp
// Custom theme for consistent styling
import com.travelassistant.ui.theme.TravelAssistantTheme

/**
 * A loading state component that displays a circular progress indicator with a message
 * Used to indicate loading or processing states in the app
 * 
 * @param message Text to display below the loading indicator
 * @param modifier Optional modifier for customizing the layout
 */
@Composable
fun LoadingState(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Circular progress indicator
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        
        // Spacing between indicator and text
        Spacer(modifier = Modifier.height(16.dp))
        
        // Loading message text
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Preview composable for LoadingState in both light and dark themes
 * Shows the loading indicator with a sample message
 */
@Preview(
    name = "Loading State Light",
    showBackground = true
)
@Preview(
    name = "Loading State Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoadingStatePreview() {
    TravelAssistantTheme {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            LoadingState(message = "Searching for flights...")
        }
    }
} 