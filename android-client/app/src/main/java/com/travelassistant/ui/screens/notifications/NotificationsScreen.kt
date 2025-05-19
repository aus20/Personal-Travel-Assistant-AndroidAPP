package com.travelassistant.ui.screens.notifications

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.components.common.cards.NotificationCard
import com.travelassistant.ui.components.common.cards.NotificationType
import com.travelassistant.ui.components.common.states.EmptyState

// Sample data for preview
private data class Notification(
    val type: NotificationType,
    val fromCity: String,
    val toCity: String,
    val date: String,
    val message: String,
    val actionText: String
)

private val sampleNotifications = listOf(
    Notification(
        type = NotificationType.FLIGHT_FOUND,
        fromCity = "New York",
        toCity = "London",
        date = "Mar 15 - Mar 19, 2024",
        message = "We found flights matching your search criteria! Check them out before they're gone.",
        actionText = "View Flights"
    ),
    Notification(
        type = NotificationType.DEPARTURE_APPROACHING,
        fromCity = "Tokyo",
        toCity = "Paris",
        date = "Apr 20 - Apr 30, 2024",
        message = "Your departure date is approaching. We haven't found exact matches, but here are the best available options.",
        actionText = "See Options"
    ),
    Notification(
        type = NotificationType.SEARCH_UPDATED,
        fromCity = "Sydney",
        toCity = "Dubai",
        date = "May 10 - May 25, 2024",
        message = "New flights have been added to your search. Prices are trending lower!",
        actionText = "Check Updates"
    )
)

@Composable
fun NotificationsScreen() {
    var notifications by remember { mutableStateOf(sampleNotifications) }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier 
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )
            
            if (notifications.isEmpty()) {
                EmptyState(
                    icon = { /* Icon will be handled by EmptyState */ },
                    title = "No Notifications",
                    description = "You'll receive notifications when we find flights matching your saved searches!"
                )
            } else {
                LazyColumn {
                    items(notifications) { notification ->
                        NotificationCard(
                            type = notification.type,
                            fromCity = notification.fromCity,
                            toCity = notification.toCity,
                            date = notification.date,
                            message = notification.message,
                            onActionClick = {
                                // Handle notification action click
                                notifications = notifications.filter { it != notification }
                            },
                            actionText = notification.actionText
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
} 