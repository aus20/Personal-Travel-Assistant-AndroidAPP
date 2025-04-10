package com.travelassistant.ui.screens.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                    .padding(16.dp)
            ) {
                // Text styles
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Headline Medium",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Headline Small",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Buttons
                Button(
                    onClick = { /* Handle click */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Primary Button")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = { /* Handle click */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Secondary Button")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                TextButton(
                    onClick = { /* Handle click */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Text Button")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Cards
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Card Content",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Status indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Left",
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                    Text(
                        text = "Right",
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusColorBox(
    color: Color,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(0.5f)
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