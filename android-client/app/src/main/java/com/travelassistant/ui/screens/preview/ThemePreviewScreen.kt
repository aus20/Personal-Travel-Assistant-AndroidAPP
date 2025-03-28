package com.travelassistant.ui.screens.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.travelassistant.ui.theme.TravelAssistantTheme

@Composable
fun ThemePreviewScreen() {
    TravelAssistantTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Typography Preview
                Text(
                    text = "Typography Preview",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Headline Medium",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Title Large",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Body Large",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Button Preview
                Text(
                    text = "Button Preview",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Primary Button")
                }
                
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Secondary Button")
                }
                
                TextButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Text Button")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Card Preview
                Text(
                    text = "Card Preview",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Flight Card Example",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "From: New York (JFK)\nTo: London (LHR)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "$599",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Status Colors Preview
                Text(
                    text = "Status Colors Preview",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusColorBox(
                        color = MaterialTheme.colorScheme.success,
                        label = "Success"
                    )
                    StatusColorBox(
                        color = MaterialTheme.colorScheme.error,
                        label = "Error"
                    )
                    StatusColorBox(
                        color = MaterialTheme.colorScheme.warning,
                        label = "Warning"
                    )
                    StatusColorBox(
                        color = MaterialTheme.colorScheme.info,
                        label = "Info"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusColorBox(
    color: androidx.compose.ui.graphics.Color,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
} 