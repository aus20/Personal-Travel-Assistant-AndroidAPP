package com.travelassistant.ui.components.common.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    date: Date?,
    dateInMillis : Long,
    onDateSelected: (Date) -> Unit,
    showDatePicker : Boolean = false,
    onShowDatePickerStatusChanged : (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }


    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = date?.let { dateFormat.format(it) } ?: "",
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) {
                    onShowDatePickerStatusChanged.invoke(true)
                },
            placeholder = { Text("Select date") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            singleLine = true
        )
    }

    if (showDatePicker && dateInMillis > 0) {
        val datePickerState = rememberDatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = dateInMillis
        )

            DatePickerDialog(
                onDismissRequest = {
                    onShowDatePickerStatusChanged(false)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                if (millis >= dateInMillis) {
                                    onDateSelected(Date(millis))
                                }
                                onShowDatePickerStatusChanged(false)
                            }
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        onShowDatePickerStatusChanged(false)
                    }) {
                        Text("Cancel")
                    }
                }
            ) {
                Box(contentAlignment = Alignment.TopEnd){
                    DatePicker(state = datePickerState)
                    Text(text = label, modifier = Modifier.padding(10.dp), fontSize = 22.sp)
                }
            }

    }
}

@Composable
fun DateRangePicker(
    departureDate: String?,
    onDepartureDateSelected: (Date) -> Unit,
    returnDate: String?,
    onReturnDateSelected: (Date) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var showDepartureDatePicker by remember { mutableStateOf(false) }
    var showReturnDatePicker by remember { mutableStateOf(false) }

    val initialSelectedDateMillis by remember {
        mutableStateOf(
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        )
    }

    var initialReturnDateMillis by remember {mutableStateOf(0L) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        DatePickerField(
            label = "Departure",
            date = parseDateFromString(departureDate),
            dateInMillis = initialSelectedDateMillis,
            onDateSelected = {
                onDepartureDateSelected.invoke(it)
                showDepartureDatePicker = false
                showReturnDatePicker = true
                initialReturnDateMillis = it.time
            },
            showDatePicker = showDepartureDatePicker,
            onShowDatePickerStatusChanged = {
                showDepartureDatePicker = it
            },
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        
        Spacer(modifier = Modifier.width(16.dp))

        DatePickerField(
            label = "Return",
            date = parseDateFromString(returnDate),
            dateInMillis = initialReturnDateMillis,
            onDateSelected = onReturnDateSelected,
            showDatePicker = showReturnDatePicker,
            onShowDatePickerStatusChanged = {
                showReturnDatePicker = it
            },
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }
}


fun parseDateFromString(dateString: String?): Date? {
    if (dateString.isNullOrBlank()) return null
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return try {
        dateFormat.parse(dateString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}