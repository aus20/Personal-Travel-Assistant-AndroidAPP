package com.travelassistant.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.travelassistant.data.local.entity.SearchDetails
import com.travelassistant.ui.components.common.buttons.PrimaryButton
import com.travelassistant.ui.components.features.search.FlightSearchForm
import com.travelassistant.ui.viewmodel.UiState
import com.travelassistant.ui.viewmodel.search.SearchData
import com.travelassistant.ui.viewmodel.search.SearchEvent
import com.travelassistant.ui.viewmodel.search.SearchViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SearchScreen(
    from: String = "",
    to: String = "",
    departureDate: String = "",
    returnDate: String = "",
    passengerCount: Int = 1,
    onclickToResultScreen : (SearchDetails) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {

    /*
    var isFromCityModalOpen by remember { mutableStateOf(false) }
    var isToCityModalOpen by remember { mutableStateOf(false) }


    if (isFromCityModalOpen || isToCityModalOpen){
        CityPickerTextField(
            onSelected = { city : String ->
                if (isFromCityModalOpen){
                    isFromCityModalOpen = false
                    viewModel.onEvent(SearchEvent.UpdateFromLocation(city))
                }else{
                    isToCityModalOpen = false
                    viewModel.onEvent(SearchEvent.UpdateToLocation(city))
                }
            },
            onDismiss = {
                isFromCityModalOpen = false
                isToCityModalOpen = false
            }
        )
    }
     */



    val state = viewModel._state.collectAsState(UiState.Success<SearchData>(SearchData()))
    LaunchedEffect(Unit) {
        viewModel.onEvent(SearchEvent.UpdateFromLocation(from))
        viewModel.onEvent(SearchEvent.UpdateToLocation(to))
        viewModel.onEvent(SearchEvent.UpdateDepartureDate(departureDate))
        viewModel.onEvent(SearchEvent.UpdateReturnDate(returnDate))
        viewModel.onEvent(SearchEvent.UpdatePassengerCount(passengerCount))
    }

    Scaffold { paddingValues ->
        state?.let {
            when(val data = state.value){
                is UiState.Error -> {

                }
                UiState.Initial -> {

                }
                UiState.Loading -> {

                }
                is UiState.Success<SearchData> -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))

                        // Header with icon
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlightTakeoff,
                                    contentDescription = "Flight Search",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "Find Your Perfect Flight",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        FlightSearchForm(
                            fromLocation = data.data.fromLocation,
                            //onOpenFromCityBottomBar = {
                            //    isFromCityModalOpen = true
                            //},
                            onFromLocationChange = { newLocation ->
                                viewModel.onEvent(SearchEvent.UpdateFromLocation(newLocation))
                            },
                            toLocation = data.data.toLocation,
                            //onOpenToCityBottomBar = {
                            //    isToCityModalOpen = true
                            //},
                            onToLocationChange = { newLocation ->
                                viewModel.onEvent(SearchEvent.UpdateToLocation(newLocation))
                            },
                            departureDate = data.data.departureDate,
                            onDepartureDateSelected = {
                                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                val formattedDate = dateFormat.format(it)
                                viewModel.onEvent(SearchEvent.UpdateDepartureDate(formattedDate))
                            },
                            returnDate = data.data.returnDate,
                            onReturnDateSelected = {
                                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                val formattedDate = dateFormat.format(it)
                                viewModel.onEvent(SearchEvent.UpdateReturnDate(formattedDate))
                            },
                            passengerCount = data.data.passengerCount,
                            onPassengerCountChange = {
                                viewModel.onEvent(SearchEvent.UpdatePassengerCount(it))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PrimaryButton(
                            text = "Search Flights",
                            onClick = {
                                val entity = viewModel.getSearchRequestModel()
                                entity?.let(onclickToResultScreen)
                            },
                            enabled = data.data.fromLocation.isNotBlank() && data.data.toLocation.isNotBlank() && data.data.departureDate != null
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun CityPickerTextField(onSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val cities = listOf(
        "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın",
        "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum",
        "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun",
        "Gümüşhane", "Hakkari", "Hatay", "Isparta", "Mersin", "Istanbul", "İzmir", "Kars", "Kastamonu",
        "Kayseri", "Kırklareli", "Kırşehir", "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş",
        "Mardin", "Muğla", "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop",
        "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak",
        "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır",
        "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sheetState) {
        sheetState.show()
    }
    ModalBottomSheet(
        onDismissRequest = {
            coroutineScope.launch {
                sheetState.hide()
                onDismiss.invoke()
            }
        },
        sheetState = sheetState
    ) {
        LazyColumn {
            items(cities) { city ->
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelected.invoke(city)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                    headlineContent = { Text(city) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreen(
            from = "from",
            to = "to",
            departureDate = "departure",
            returnDate = "returnDate",
            passengerCount = 2,
            onclickToResultScreen = {},
        )
    }
}