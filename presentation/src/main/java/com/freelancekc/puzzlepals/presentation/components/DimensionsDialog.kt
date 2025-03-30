package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DimensionsDialog(
    rows: Float,
    columns: Float,
    onRowsChange: (Float) -> Unit,
    onColumnsChange: (Float) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Puzzle Dimensions") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Rows: ${rows.toInt()}")
                Slider(
                    value = rows,
                    onValueChange = onRowsChange,
                    valueRange = 2f..6f,
                    steps = 3
                )
                Text("Columns: ${columns.toInt()}")
                Slider(
                    value = columns,
                    onValueChange = onColumnsChange,
                    valueRange = 2f..6f,
                    steps = 3
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Create Puzzle")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 