package com.travelassistant.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.travelassistant.data.model.FlightResult
import com.travelassistant.ui.components.common.buttons.PrimaryButton
import com.travelassistant.ui.navigation.Screen // Your Screen sealed class
import com.travelassistant.ui.screens.flight.FlightDetailsScreen
import com.travelassistant.ui.theme.TravelAssistantTheme
import com.travelassistant.ui.viewmodel.UiState // Your UiState sealed class
import com.travelassistant.ui.viewmodel.register.RegisterDataState
import com.travelassistant.ui.viewmodel.register.RegisterEvent
import com.travelassistant.ui.viewmodel.register.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // Observe the UiState<RegisterDataState> from the ViewModel
    val uiState by viewModel._state.collectAsState(UiState.Initial)
    val context = LocalContext.current

    // Effect to handle navigation or one-time messages upon successful registration
    LaunchedEffect(key1 = uiState) {
        if (uiState is UiState.Success) {
            val successState = (uiState as UiState.Success<RegisterDataState>).data
            if (successState.registrationMessage != null) {
                // Show a message (e.g., Toast) and navigate
                Toast.makeText(context, successState.registrationMessage, Toast.LENGTH_LONG).show()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Register.route) { inclusive = true } // Clear register from backstack
                }
                // Optionally reset the message in ViewModel to prevent re-triggering on recomposition
                // viewModel.onEvent(RegisterEvent.RegistrationMessageShown) // You'd need to add this event
            }
        }
    }

    TravelAssistantTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Create Account") })
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (val currentState = uiState) {
                    is UiState.Initial, is UiState.Success -> {
                        // In Success state, currentState.data is RegisterDataState
                        // In Initial, we might want to show the form with default data
                        val registerData = (currentState as? UiState.Success<RegisterDataState>)?.data
                            ?: RegisterDataState() // Default if initial or other non-success state that implies form display

                        RegisterFormContent(
                            registerDataState = registerData,
                            onEvent = viewModel::onEvent,
                            isLoading = false, // Not loading if in Success or Initial
                            generalErrorMessage = null,
                            navController = navController
                        )
                    }
                    is UiState.Loading -> {
                        // Show form content but with a loading indicator active
                        // Or a full screen loader. For this example, we pass isLoading to the form.
                        val previousData = RegisterDataState() // Show empty form or ideally cache last known data
                        RegisterFormContent(
                            registerDataState = previousData, // Or extract from previous success state if available
                            onEvent = viewModel::onEvent,
                            isLoading = true,
                            generalErrorMessage = null,
                            navController = navController
                        )
                    }
                    is UiState.Error -> {
                        // Show form content but with an error message
                        val previousData = RegisterDataState() // Show empty form or cache last known data
                        RegisterFormContent(
                            registerDataState = previousData,
                            onEvent = viewModel::onEvent,
                            isLoading = false,
                            generalErrorMessage = currentState.message,
                            navController = navController
                        )
                    }


                }
            }
        }
    }
}

@Composable
fun RegisterFormContent(
    modifier: Modifier = Modifier,
    registerDataState: RegisterDataState,
    onEvent: (RegisterEvent) -> Unit,
    isLoading: Boolean,
    generalErrorMessage: String?,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = registerDataState.name,
            onValueChange = { onEvent(RegisterEvent.NameChanged(it)) },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
            // You can add specific error display for fields if your RegisterDataState includes them
        )

        OutlinedTextField(
            value = registerDataState.email,
            onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = !isLoading
            // isError = some email specific error flag from registerDataState
        )

        OutlinedTextField(
            value = registerDataState.password,
            onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
            label = { Text("Password (min. 8 characters)") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !isLoading
            // isError = some password specific error flag from registerDataState
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
        }

        // Display general error message from UiState.Error
        generalErrorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Display specific success/error messages from RegisterDataState if any
        // (Currently, registrationMessage is used for success)
        // registerDataState.registrationMessage could also be used for specific validation errors.

        PrimaryButton(
            text = "Sign Up",
            onClick = { onEvent(RegisterEvent.SignUpClicked) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            enabled = registerDataState.isSignUpButtonEnabled && !isLoading
        )

        TextButton(
            onClick = {
                navController.navigate(Screen.Login.route) {
                    // Optional: popUpTo(Screen.Register.route) { inclusive = true } if you want
                    // clicking this to also remove register from backstack immediately.
                    // Or just let it be on the backstack if user wants to go "back" to register from login.
                    // For a direct link, usually no popUpTo is needed here, just navigate.
                }
                // TODO: Navigate to Login Screen
                // This requires NavController to be passed down or an event sent up
                // For simplicity, if RegisterScreen manages navigation, it's fine.
                // If this component is highly reusable, it might need an onLoginClicked lambda.
            },
            enabled = !isLoading
        ) {
            Text("Already have an account? Log In")
        }
    }
}


@Preview(showBackground = true, name = "Flight Details - With Stops")
@Composable
fun RegisterScreenWithStopsPreview() {
    MaterialTheme {
        val navController = rememberNavController()
        RegisterScreen(navController)
    }
}