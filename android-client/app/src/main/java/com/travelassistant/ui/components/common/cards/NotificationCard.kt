package com.travelassistant.ui.components.common.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

enum class NotificationType {
    FLIGHT_FOUND,
    DEPARTURE_APPROACHING,
    SEARCH_UPDATED
}

@Composable
fun NotificationCard(
    type: NotificationType,
    fromCity: String,
    toCity: String,
    date: String,
    message: String,
    onActionClick: () -> Unit,
    actionText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
                Icon(
                    imageVector = when (type) {
                        NotificationType.FLIGHT_FOUND -> Icons.Default.Search
                        NotificationType.DEPARTURE_APPROACHING -> Icons.Default.FlightTakeoff
                        NotificationType.SEARCH_UPDATED -> Icons.Default.Warning
                    },
                    contentDescription = null,
                    tint = when (type) {
                        NotificationType.FLIGHT_FOUND -> MaterialTheme.colorScheme.primary
                        NotificationType.DEPARTURE_APPROACHING -> MaterialTheme.colorScheme.error
                        NotificationType.SEARCH_UPDATED -> MaterialTheme.colorScheme.tertiary
                    }
                )
                
                Spacer(modifier = Modifier.padding(horizontal = 12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$fromCity → $toCity",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(
                onClick = onActionClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = actionText)
            }
        }
    }
} 