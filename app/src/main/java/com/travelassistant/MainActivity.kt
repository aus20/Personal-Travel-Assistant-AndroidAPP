package com.travelassistant

// Android core components
import android.os.Bundle
// Base activity class for Compose
import androidx.activity.ComponentActivity
// Compose integration with Activity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
// Our test screen component
// App theme
import com.travelassistant.ui.theme.TravelAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.navigation.compose.rememberNavController
import com.travelassistant.data.session.SessionManager
import com.travelassistant.ui.components.navigation.BottomNavigationBar
import com.travelassistant.ui.navigation.RootNavGraph
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

  // private val authViewModel: AuthViewModel by viewModels() // AuthViewModel'i Hilt ile al
  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      SessionManager.userDetail.value = sessionManager.getUserDetails()
      setContent {
          TravelAssistantTheme {
              val navController = rememberNavController()
              val isLogin = SessionManager.userDetail.collectAsState().value != null
              Scaffold(
                  bottomBar = {
                      if (isLogin) {
                          BottomNavigationBar(navController = navController)
                      }
                  }
              ) { padding ->
                  RootNavGraph(
                      isLogin = isLogin,
                      navController = navController,
                      modifier = Modifier.padding(padding)
                  ) // Call your root graph
              }
          }
      }
  }
}
