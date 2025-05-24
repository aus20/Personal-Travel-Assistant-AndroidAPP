package com.travelassistant.ui.components.features.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.buttons.PrimaryButton
import com.travelassistant.ui.viewmodel.search.FlightFilters


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    availableAirlines: List<String>,
    currentFilters: FlightFilters,
    onDismiss: () -> Unit,
    onApplyFilters: (FlightFilters) -> Unit,
    onResetFilters: () -> Unit
) {
    // Local state for filters
    var priceRange by remember { mutableStateOf(currentFilters.priceRange) }
    var maxStops by remember { mutableStateOf(currentFilters.maxStops) }
    var selectedAirlines by remember { mutableStateOf(currentFilters.selectedAirlines) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Flights",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            
            Divider()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Price Range Filter
            Text(
                text = "Price Range",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$${priceRange.start.toInt()} - $${priceRange.endInclusive.toInt()}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            RangeSlider(
                value = priceRange,
                onValueChange = { priceRange = it },
                valueRange = 0f..1000f,
                steps = 20,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Stops Filter
            Text(
                text = "Maximum Stops",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(0, 1, 2).forEach { stops ->
                    OutlinedButton(
                        onClick = { maxStops = stops },
                        modifier = Modifier.weight(1f),
                        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                            containerColor = if (maxStops == stops) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = when (stops) {
                                0 -> "Non-stop"
                                1 -> "1 Stop"
                                else -> "2+ Stops"
                            }
                        )
                    }
                    
                    if (stops < 2) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Airlines Filter
            Text(
                text = "Airlines",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            availableAirlines.forEach { airline ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Checkbox(
                        checked = selectedAirlines.contains(airline),
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedAirlines = selectedAirlines + airline
                            } else {
                                selectedAirlines = selectedAirlines - airline
                            }
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = airline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onResetFilters,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                PrimaryButton(
                    text = "Apply Filters",
                    onClick = {
                        onApplyFilters(
                            FlightFilters(
                                priceRange = priceRange,
                                maxStops = maxStops,
                                selectedAirlines = selectedAirlines
                            )
                        )
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
} 