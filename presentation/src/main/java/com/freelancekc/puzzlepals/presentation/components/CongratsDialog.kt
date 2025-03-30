package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CongratsDialog(
    onDismiss: () -> Unit,
    onLeave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Congratulations!") },
        text = { Text("You've completed the puzzle!") },
        confirmButton = {
            Button(onClick = onLeave) {
                Text("Leave")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CongratsDialogPreview() {
    MaterialTheme {
        CongratsDialog(
            onDismiss = {},
            onLeave = {}
        )
    }
}