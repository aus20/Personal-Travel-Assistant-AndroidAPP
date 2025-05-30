package com.travelassistant.ui.screens.login

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.travelassistant.ui.components.common.buttons.PrimaryButton
import com.travelassistant.ui.navigation.Screen
import com.travelassistant.ui.theme.TravelAssistantTheme
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.login.LoginDataState
import com.travelassistant.ui.viewmodel.login.LoginEvent
import com.travelassistant.ui.viewmodel.login.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState) { // Re-evaluate when uiState changes
        viewModel.state.collectLatest { collectedUiState ->
            if (collectedUiState is UiState.Success) {
                val successData = collectedUiState.data
                if (successData.loginMessage == "Login successful!") {
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to the main app screen and clear the auth flow from backstack
                    navController.navigate(Screen.Search.route) {
                        // popUpTo(Screen.Login.route) { inclusive = true } // Clear Login
                        // If Register was before Login and you want to clear it too:
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        // Or, if your auth flow has its own nested graph, popUpTo that graph's route.
                    }
                    // Optionally reset loginMessage in ViewModel
                }
            }
        }
    }

    TravelAssistantTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Log In") })
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (val currentUiState = uiState) {
                    is UiState.Initial, is UiState.Success -> {
                        val loginData = (currentUiState as? UiState.Success<LoginDataState>)?.data
                            ?: LoginDataState()
                        LoginFormContent(
                            loginDataState = loginData,
                            onEvent = viewModel::onEvent,
                            isLoading = false,
                            generalErrorMessage = null,
                            navController = navController // Pass NavController for "Sign Up" navigation
                        )
                    }
                    is UiState.Loading -> {
                        LoginFormContent(
                            loginDataState = (viewModel.state.value as? UiState.Success<LoginDataState>)?.data ?: LoginDataState(),
                            onEvent = viewModel::onEvent,
                            isLoading = true,
                            generalErrorMessage = null,
                            navController = navController
                        )
                    }
                    is UiState.Error -> {
                        LoginFormContent(
                            loginDataState = LoginDataState(), // Or last known data
                            onEvent = viewModel::onEvent,
                            isLoading = false,
                            generalErrorMessage = currentUiState.message,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginFormContent(
    modifier: Modifier = Modifier,
    loginDataState: LoginDataState,
    onEvent: (LoginEvent) -> Unit,
    isLoading: Boolean,
    generalErrorMessage: String?,
    navController: NavController, // For "Sign Up" link
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
            text = "Welcome Back!",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = loginDataState.email,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = !isLoading
        )

        OutlinedTextField(
            value = loginDataState.password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !isLoading
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
        }

        generalErrorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        PrimaryButton(
            text = "Log In",
            onClick = { onEvent(LoginEvent.LoginClicked) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            enabled = loginDataState.isLoginButtonEnabled && !isLoading
        )

        TextButton(
            onClick = { navController.navigate(Screen.Register.route) }, // Navigate to Register
            enabled = !isLoading
        ) {
            Text("Don't have an account? Sign Up")
        }
    }
}