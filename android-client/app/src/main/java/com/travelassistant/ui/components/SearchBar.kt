package com.travelassistant.ui.components

// Android Configuration for handling different UI modes (e.g., dark/light theme)
import android.content.res.Configuration
// Layout composables for structuring UI elements
import androidx.compose.foundation.layout.*
// Keyboard handling utilities
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
// Material Icons for search and clear functionality
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
// Material Design 3 components and theming
import androidx.compose.material3.*
// Compose state management
import androidx.compose.runtime.*
// UI utilities
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
// Keyboard action type definitions
import androidx.compose.ui.text.input.ImeAction
// Preview annotation for Android Studio preview
import androidx.compose.ui.tooling.preview.Preview
// Dimension utilities for spacing and sizing
import androidx.compose.ui.unit.dp
// Custom theme for consistent styling
import com.travelassistant.ui.theme.TravelAssistantTheme

/**
 * A customizable search bar component with search and clear functionality
 * @param value Current text value in the search field
 * @param onValueChange Callback for when text value changes
 * @param modifier Optional modifier for customizing the layout
 * @param hint Placeholder text shown when search field is empty
 * @param onSearch Callback for when search action is triggered
 * @param onFocusChanged Callback for when focus state changes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search",
    onSearch: () -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {}
) {
    // State to control the visibility of the clear button
    var showClearButton by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            showClearButton = it.isNotEmpty()
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it) },
        // Placeholder text styling
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        // Custom colors for different states
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        // Search icon at the start
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        // Clear button at the end (shown only when text exists)
        trailingIcon = {
            if (showClearButton) {
                IconButton(
                    onClick = {
                        onValueChange("")
                        showClearButton = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        // Configure keyboard options
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        shape = MaterialTheme.shapes.medium
    )
}

/**
 * Preview composable for SearchBar in both light and dark themes
 * Shows a sample search bar with interactive state
 */
@Preview(
    name = "Search Bar Light",
    showBackground = true
)
@Preview(
    name = "Search Bar Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SearchBarPreview() {
    TravelAssistantTheme {
        Surface(
            modifier = Modifier.padding(16.dp)
        ) {
            // Local state for the preview
            var searchText by remember { mutableStateOf("") }
            SearchBar(
                value = searchText,
                onValueChange = { searchText = it },
                hint = "Search for flights"
            )
        }
    }
} 