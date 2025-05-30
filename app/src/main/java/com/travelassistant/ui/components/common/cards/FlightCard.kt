package com.travelassistant.ui.components.common.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.viewmodel.saved.SavedSearch
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FlightCard(
    search: SavedSearch,
    price: String,
    onclick: (SavedSearch) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable {
                onclick.invoke(search)
            }
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = search.fromLocation,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = search.toLocation,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.AirplanemodeActive,
                    contentDescription = "Flight",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = search.departureDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (search.returnDate == null) "One way" else "Two way",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row( // Use a Row to align icon and text
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Spacing between icon and text
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Passengers",
                            modifier = Modifier.size(16.dp), // Adjust size as needed
                            tint = MaterialTheme.colorScheme.onSurfaceVariant // Use a subtle color
                        )
                        Text(
                            text = "${search.passengerCount} ${if (search.passengerCount == 1) "passenger" else "passengers"}",
                            style = MaterialTheme.typography.bodySmall, // Smaller text for secondary info
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price Alert Active",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }


            }
        }
    }
}

// ADD: Preview Composable
@Preview(showBackground = true, name = "Flight Card Preview")
@Composable
fun FlightCardPreview() {
    // If your FlightCard relies on a specific theme (which it does via MaterialTheme),
    // wrap it in your app's theme for an accurate preview.
    // Replace YourAppTheme with the actual name of your theme composable.
    // YourAppTheme { // Optional: uncomment and use your app's theme
    FlightCard(
        search = SavedSearch( // Use the correct SavedSearch structure
            id = "previewId123",
            fromLocation = "Istanbul (IST)",
            toLocation = "Ankara (ESB)",
            departureDate = "Oct 20, 2025",
            returnDate = "Oct 27, 2025", // Can be null
            passengerCount = 2,
            lastSearchResults = null, // Or emptyList() if preferred for preview
            createdAt = "2025-05-29T10:00:00Z",
            isActive = true
        ),
        price = "€150",
        onclick = { /* Preview click does nothing */ },
        onDelete = { /* Preview delete does nothing */ }
    )
    // } // Optional: uncomment if using YourAppTheme
}