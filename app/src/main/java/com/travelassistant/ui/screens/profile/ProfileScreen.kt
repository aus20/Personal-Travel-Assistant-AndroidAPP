package com.travelassistant.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.travelassistant.ui.navigation.Screen
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.profile.ProfileNavigationEvent
import com.travelassistant.ui.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // 1. Add a state variable for the new dialog
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    if (showPrivacyPolicyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyPolicyDialog = false })
    }

    // Show the Help dialog when its state is true
    if (showHelpDialog) {
        HelpAndSupportDialog(onDismiss = { showHelpDialog = false })
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ProfileNavigationEvent.NavigateToLogin -> {
                    // Navigate to Login and clear the entire back stack
                    navController.navigate(Screen.Login.route) {
                        // This pops all screens off the stack before navigating
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        // Ensures we don't create multiple copies of the login screen
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val profileData = state.data
                    ProfileContent(
                        name = profileData.name,
                        email = profileData.email,
                        onLogoutClick = {
                            viewModel.onLogoutClicked()
                            // TODO: Add navigation back to login screen here.
                        },
                        onPrivacyPolicyClick = {
                            showPrivacyPolicyDialog = true
                        },
                        // 2. Add a click handler for the Help section
                        onHelpClick = {
                            showHelpDialog = true
                        },
                        viewModel = viewModel
                    )
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UiState.Initial -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    name: String,
    email: String,
    onLogoutClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onHelpClick: () -> Unit,
    viewModel: ProfileViewModel, // 3. Pass the new click handler down
) {
    var notificationsEnabled by remember { mutableStateOf(viewModel.sessionManager.notificationPermissionGranted) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header ...
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Settings Section ...
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Settings Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                // Push Notifications ...
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Push Notifications",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    )
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            viewModel.sessionManager.notificationPermissionGranted = it
                            notificationsEnabled = it
                        }
                    )
                }

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // Privacy Policy ...
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onPrivacyPolicyClick)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PrivacyTip,
                        contentDescription = "Privacy Policy",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Privacy Policy",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // Help & Support
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onHelpClick) // 4. Make the row clickable
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Help & Support",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Help & Support",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                /*
                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // Delete Account ...
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Handle click */ }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Account",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Delete Account",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                 */
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button ...
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(text = "Logout")
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

private const val privacyPolicyText =
    "Effective Date: May 11, 2025\n\n" +
        "This Privacy Policy describes Our policies and procedures on the collection, " +
        "use and disclosure of Your information when You use the Service and tells You " +
        "about Your privacy rights and how the law protects You.\n\n" +
        "We use Your Personal data to provide and improve the Service. By using the " +
        "Service, You agree to the collection and use of information in accordance with " +
        "this Privacy Policy.\n\n" +
        "Information We Collect\n\n" +
        "While using Our Service, We may ask You to provide Us with certain personally " +
        "identifiable information that can be used to contact or identify You. " +
        "Personally identifiable information may include, but is not limited to:\n" +
        "- Email address\n" +
        "- First name and last name\n" +
        "- Usage Data\n\n" +
        "Use of Your Personal Data\n\n" +
        "The Company may use Personal Data for the following purposes:\n" +
        "- To provide and maintain our Service, including to monitor the usage of our Service.\n" +
        "- To manage Your Account: to manage Your registration as a user of the Service.\n" +
        "- To contact You: To contact You by email, telephone calls, SMS, or other " +
        "equivalent forms of electronic communication.\n\n" +
        "We do not sell or share your personal information with third parties for " +
        "their marketing purposes. Your data is used solely for the functionality of " +
        "the Personal Travel Assistant app.\n\n" +
        "Contact Us\n\n" +
        "If you have any questions about this Privacy Policy, You can contact us: " +
        "by email at support@travelassistant.com"

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Privacy Policy") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = privacyPolicyText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}

// 5. Define the generic Help & Support text
private const val helpAndSupportText =
    "Welcome to the Help & Support Center.\n\n" +
            "**Frequently Asked Questions (FAQ)**\n\n" +
            "**Q: How do I search for a flight?**\n" +
            "A: Navigate to the 'Search' screen from the bottom navigation bar. " +
            "Enter your departure and destination airports, select your dates, " +
            "and tap the search button to see available flights.\n\n" +
            "**Q: How can I save a search?**\n" +
            "A: After performing a search, you can tap the 'Save Search' button " +
            "on the results screen. You can view your saved searches on the " +
            "'Saved' screen.\n\n" +
            "**Q: How do I manage notifications?**\n" +
            "A: You can enable or disable push notifications from this settings screen. " +
            "We will notify you about price drops and flight status changes for your saved searches.\n\n" +
            "**Contact Us**\n\n" +
            "If you need further assistance or have any feedback, please do not hesitate to contact us. " +
            "Our support team is available 24/7.\n\n" +
            "- **Email:** support@travelassistant.com\n"



// 6. Create a new Composable for the Help & Support AlertDialog
@Composable
fun HelpAndSupportDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Help & Support")
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = helpAndSupportText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    ProfileContent(
        name = "Alperen Us",
        email = "alperen.us@gmail.com",
        onLogoutClick = { },
        onPrivacyPolicyClick = { },
        onHelpClick = { },
        viewModel = viewModel
    )
}