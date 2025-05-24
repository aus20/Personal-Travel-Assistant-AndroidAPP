package com.travelassistant.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import com.travelassistant.ui.screens.search.SearchScreen
import com.travelassistant.ui.screens.search.SearchResultsScreen
import com.travelassistant.ui.screens.saved.SavedSearchesScreen
import com.travelassistant.ui.screens.notifications.NotificationsScreen
import com.travelassistant.ui.screens.profile.ProfileScreen
import com.travelassistant.ui.screens.flight.FlightDetailsScreen
import com.travelassistant.data.model.FlightResult
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.notifications.NotificationsViewModel
import com.travelassistant.ui.viewmodel.profile.ProfileViewModel
import com.travelassistant.ui.viewmodel.saved.SavedSearchesEvent
import com.travelassistant.ui.viewmodel.saved.SavedSearchesState
import com.travelassistant.ui.viewmodel.saved.SavedSearchesViewModel
import com.travelassistant.ui.viewmodel.search.FlightFilters
import com.travelassistant.ui.viewmodel.search.SearchData
import com.travelassistant.ui.viewmodel.search.SearchEvent
import com.travelassistant.ui.viewmodel.search.SearchViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Search.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Search.route) {
            val searchViewModel: SearchViewModel = hiltViewModel()
            val uiState by searchViewModel.state.collectAsState()
            when (val currentState = uiState) {
                is UiState.Success<SearchData> -> {
                    val searchData = currentState.data
                    SearchScreen(
                        fromLocation = searchData.fromLocation,
                        toLocation = searchData.toLocation,
                        departureDate = searchData.departureDate,
                        returnDate = searchData.returnDate,
                        passengerCount = searchData.passengerCount,
                        onFromLocationChange = { searchViewModel.onEvent(SearchEvent.UpdateFromLocation(it)) },
                        onToLocationChange = { searchViewModel.onEvent(SearchEvent.UpdateToLocation(it)) },
                        onDepartureDateChange = { searchViewModel.onEvent(SearchEvent.UpdateDepartureDate(it)) },
                        onReturnDateChange = { searchViewModel.onEvent(SearchEvent.UpdateReturnDate(it)) },
                        onPassengerCountChange = { searchViewModel.onEvent(SearchEvent.UpdatePassengerCount(it)) },
                        onSearchClick = {
                            if (searchData.isFormValid) { // ViewModel'daki isFormValid kontrolü
                                searchViewModel.onEvent(SearchEvent.PerformSearch)
                                // Navigasyon, SearchViewModel içinde bir event ile yönetilebilir
                                // VEYA arama sonuçları geldiğinde SearchData güncellenince
                                // bu LaunchedEffect ile navigasyon yapılabilir.
                                // Şimdilik, navigasyonun doğrudan SearchScreen'den tetiklendiğini varsayalım.
                                val departureDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(searchData.departureDate!!)
                                val returnDateStr = searchData.returnDate?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) }

                                navController.navigate(
                                    Screen.SearchResults.createRoute(
                                        fromCity = searchData.fromLocation,
                                        toCity = searchData.toLocation,
                                        departureDate = departureDateStr,
                                        returnDate = returnDateStr // createRoute null'ı handle etmeli
                                    )
                                )
                            } else {
                                // Kullanıcıya formun geçerli olmadığına dair bir uyarı gösterilebilir.
                                // Bu genellikle SearchScreen içinde halledilir.
                            }
                        }
                    )
                }
                is UiState.Loading -> {
                    // Yükleniyor... (Örn: com.travelassistant.ui.components.LoadingState())
                    // SearchScreen kendi içinde bir yükleme durumu yönetebilir.
                }
                is UiState.Error -> {
                    Text("Hata: ${currentState.message}")
                }
                UiState.Initial -> {
                    // Başlangıç durumu, belki SearchScreen'e boş SearchData ile başlamak için
                    // SearchViewModel'in init'inde Success(SearchData()) yapılıyor.
                }
            }
        }

        composable(
            route = Screen.SearchResults.route,
            arguments = listOf(
                navArgument("fromCity") { type = NavType.StringType },
                navArgument("toCity") { type = NavType.StringType },
                navArgument("departureDate") { type = NavType.StringType },
                navArgument("returnDate") { type = NavType.StringType; nullable = true; defaultValue = "" }
            )
        ) { backStackEntry ->
            // SearchResultsScreen genellikle SearchViewModel'dan veri alır.
            // Çünkü arama kriterleri ve sonuçları zaten o ViewModel'da.
            val searchViewModel: SearchViewModel = hiltViewModel()
            val uiState by searchViewModel.state.collectAsState()

            val fromCityArg = backStackEntry.arguments?.getString("fromCity") ?: ""
            val toCityArg = backStackEntry.arguments?.getString("toCity") ?: ""
            val departureDateStrArg = backStackEntry.arguments?.getString("departureDate") ?: ""
            val returnDateStrArg = backStackEntry.arguments?.getString("returnDate")

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val departureDateArg = try { dateFormat.parse(departureDateStrArg) ?: Date() } catch (e: Exception) { Date() }
            val returnDateArg = if (!returnDateStrArg.isNullOrEmpty()) {
                try { dateFormat.parse(returnDateStrArg) } catch (e: Exception) { null }
            } else { null }

            when (val currentState = uiState) {
                is UiState.Success<SearchData> -> {
                    val searchData = currentState.data
                    // SearchViewModel'dan isOnline durumunu da almamız lazım.
                    // SearchData içine eklenebilir veya ViewModel'dan ayrı bir StateFlow olarak alınabilir.
                    // val isOnline by searchViewModel.isOnline.collectAsState() // Eğer ViewModel'da böyle bir state varsa

                    SearchResultsScreen(
                        fromCity = fromCityArg,
                        toCity = toCityArg,
                        departureDate = departureDateArg,
                        returnDate = returnDateArg,
                        flights = searchData.searchResults as List<FlightResult>,
                        isLoading = false, // UiState.Loading ile bu ekranın dışında yönetiliyor
                        isOnline = true, // TODO: searchViewModel.isOnline'dan al
                        currentFilters = searchData.filters,
                        selectedSortOption = searchData.sortOption,
                        showSortMenu = false, // Bu UI state'i SearchResultsScreen kendi içinde yönetebilir
                        showFilterSheet = false, // Bu UI state'i SearchResultsScreen kendi içinde yönetebilir
                        availableAirlines = searchData.availableAirlines, // ViewModel doldurmalı
                        onBackClick = { navController.popBackStack() },
                        onFlightClick = { flight ->
                            navController.navigate(Screen.FlightDetails.createRoute(flight.id))
                        },
                        onSortOptionSelected = { searchViewModel.onEvent(SearchEvent.UpdateSortOption(it)) },
                        onSortMenuVisibilityChanged = { /* UI state'ini yönet */ },
                        onFilterSheetVisibilityChanged = { /* UI state'ini yönet */ },
                        onFiltersApplied = { searchViewModel.onEvent(SearchEvent.UpdateFilters(it)) },
                        onFiltersReset = {
                            searchViewModel.onEvent(SearchEvent.UpdateFilters(FlightFilters())) // Varsayılan filtreler
                        }
                    )
                }
                is UiState.Loading -> {
                    // com.travelassistant.ui.components.LoadingState()
                }
                is UiState.Error -> {
                    Text("Hata: ${currentState.message}")
                }
                UiState.Initial -> {
                    // Belki bir yükleme ekranı veya boş durum
                }
            }
        }
        composable(
            route = Screen.FlightDetails.route,
            arguments = listOf(
                navArgument("flightId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val flightId = backStackEntry.arguments?.getString("flightId") ?: ""
            // TODO: Gerçek FlightResult'ı flightId ile ViewModel üzerinden çekin.
            // Şimdilik SearchViewModel'daki sonuçlardan bulmayı deneyebiliriz veya ayrı bir ViewModel.
            // Bu kısım için ayrı bir FlightDetailsViewModel daha uygun olabilir.
            // Geçici çözüm olarak, bir önceki ekrandan (SearchResults) gelen listeden bulunabilir
            // AMA bu iyi bir pratik değil. En iyisi flightId ile yeniden fetch etmek.

            val dummyFlight = FlightResult(
                id = flightId,
                airline = "Delta Airlines",
                airlineLogo = "delta_logo",
                flightNumber = "DL123",
                departureAirport = "JFK",
                arrivalAirport = "LAX",
                departureTime = Date(),
                arrivalTime = Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000),
                duration = "5h 30m",
                stops = 0,
                price = 299.99,
                currency = "USD"
            )
            
            FlightDetailsScreen(
                flight = dummyFlight,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.SavedSearches.route) {
            val savedSearchesViewModel: SavedSearchesViewModel = hiltViewModel()
            val uiState by savedSearchesViewModel.state.collectAsState()

            when (val currentState = uiState) {
                is UiState.Success<SavedSearchesState> -> {
                    val savedState = currentState.data
                    if (savedState is SavedSearchesState.Success) {
                        SavedSearchesScreen(
                            // SavedSearchesScreen'in beklediği model ile ViewModel'daki model uyumlu olmalı
                            // veya burada mapleme yapılmalı.
                            savedSearches = savedState.savedSearches.map { domainModel ->
                                // Bu mapleme SavedSearchesScreen.kt'deki SavedSearch data class'ına göre olmalı
                                com.travelassistant.ui.screens.saved.SavedSearch(
                                    from = domainModel.fromLocation,
                                    to = domainModel.toLocation,
                                    // date formatlaması örnektir, UI modelinize göre ayarlayın
                                    date = "${SimpleDateFormat("MMM dd", Locale.getDefault()).format(domainModel.departureDate)}${domainModel.returnDate?.let {" - ${SimpleDateFormat("MMM dd", Locale.getDefault()).format(it)}"} ?: ""}"
                                )
                            },
                            onDeleteSearch = { searchUiModel ->
                                // UI modelinden domain model ID'sini bulup silme event'i gönder
                                val originalSearch = savedState.savedSearches.find {
                                    it.fromLocation == searchUiModel.from && it.toLocation == searchUiModel.to
                                    // Daha güvenilir bir eşleştirme için ID kullanılmalı.
                                    // SavedSearch UI modeline ID eklenip onunla eşleştirme yapılabilir.
                                }
                                originalSearch?.let {
                                    savedSearchesViewModel.onEvent(SavedSearchesEvent.DeleteSearch(it.id))
                                }
                            }
                        )
                    } else if (savedState is SavedSearchesState.Loading) {
                        // Yükleniyor...
                    } else if (savedState is SavedSearchesState.Error) {
                        Text("Hata: ${savedState.message}")
                    } else {
                        // Empty state vs.
                        SavedSearchesScreen(savedSearches = emptyList(), onDeleteSearch = {})
                    }
                }
                is UiState.Loading -> { /* Yükleniyor... */ }
                is UiState.Error -> { Text("Hata: ${currentState.message}") }
                UiState.Initial -> { /* Başlangıç... */ }
            }
        }

        composable(Screen.Notifications.route) {
            val notificationsViewModel: NotificationsViewModel = hiltViewModel()
            // NotificationsScreen kendi içinde state'i ve event'leri yönetiyor olabilir
            // veya ViewModel'dan alabilir. Mevcut NotificationsScreen.kt'ye göre
            // dışarıdan bir state beklemiyor gibi duruyor ama ideali ViewModel ile yönetmek.
            // Şimdilik direkt çağırıyoruz, gerekirse ViewModel entegrasyonu eklenecek.
            NotificationsScreen()
            // Örnek ViewModel kullanımı:
            // val uiState by notificationsViewModel.state.collectAsState()
            // when (val currentState = uiState) { ... }
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            // Benzer şekilde ProfileScreen de ViewModel ile yönetilmeli.
            // Şimdilik direkt çağırıyoruz.
            ProfileScreen()
            // Örnek ViewModel kullanımı:
            // val uiState by profileViewModel.state.collectAsState()
            // when (val currentState = uiState) { ... }
        }

    }
}