package com.travelassistant.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
// import androidx.compose.runtime.getValue // Not used directly in this class
// import androidx.compose.runtime.mutableStateOf // Not used directly in this class
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.qualifiers.ApplicationContext // Import ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject // Import Inject
import javax.inject.Singleton // Import Singleton

@Singleton // Make it a Singleton
class NetworkStateManager @Inject constructor( // Add @Inject constructor
    @ApplicationContext context: Context // Hilt will provide the application context
) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isOnline = MutableStateFlow(true) // Consider initializing based on current state
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isOnline.value = true
        }

        override fun onLost(network: Network) {
            _isOnline.value = false
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            _isOnline.value = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) // Check for actual internet access
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Check initial state more reliably
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        _isOnline.value = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
    }

    // REMOVE the companion object with getInstance
    /*
    companion object {
        @Volatile
        private var INSTANCE: NetworkStateManager? = null

        fun getInstance(context: Context): NetworkStateManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkStateManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    */
}

// The Composable function can remain the same, but it will now get the Hilt-provided singleton.
// To use this in a Composable, you might need to inject NetworkStateManager into your ViewModel
// or provide it differently if you want to keep this exact rememberNetworkState() utility.
// For now, let's focus on making it injectable for repositories.
// If needed later, you can have:
// @Composable
// fun currentNetworkState(): State<Boolean> {
//    val networkStateManager: NetworkStateManager = hiltViewModel<SomeViewModel>().networkStateManager // or get from an injected point
//    return networkStateManager.isOnline.collectAsState()
// }
// However, the existing rememberNetworkState() might still work if it reconstructs
// using the application context to get the Hilt singleton, or you can adjust how it's used.
// The critical part is that NetworkStateManager is now a Hilt Singleton.