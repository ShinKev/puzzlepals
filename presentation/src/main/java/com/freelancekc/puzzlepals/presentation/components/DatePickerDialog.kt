package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar

private fun Calendar.toEpochMillis(): Long {
    return this.timeInMillis
}

private fun Long.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = this@toCalendar
    }
}

private fun Calendar.isOlderThanThreeDays(): Boolean {
    val twoDaysAgo = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -3)
    }
    return this.timeInMillis < twoDaysAgo.timeInMillis
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    selectedDate: Calendar,
    onDismiss: () -> Unit,
    onDateSelected: (Calendar) -> Unit,
    onSubscribeToPremium: () -> Unit
) {
    var showPremiumDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toEpochMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= Calendar.getInstance().timeInMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedCalendar = millis.toCalendar()
                            if (selectedCalendar.isOlderThanThreeDays()) {
                                showPremiumDialog = true
                            } else {
                                onDateSelected(selectedCalendar)
                            }
                        }
                        onDismiss()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = null,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }

    if (showPremiumDialog) {
        PremiumSubscriptionDialog(
            onDismiss = { showPremiumDialog = false },
            onSubscribe = {
                showPremiumDialog = false
                onSubscribeToPremium()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerDialogPreview() {
    DatePickerDialog(
        showDialog = true,
        selectedDate = Calendar.getInstance(),
        onDismiss = {},
        onDateSelected = {},
        onSubscribeToPremium = {}
    )
} 