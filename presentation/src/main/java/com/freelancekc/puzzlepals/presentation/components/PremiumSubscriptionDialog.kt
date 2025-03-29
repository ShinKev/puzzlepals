package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PremiumSubscriptionDialog(
    onDismiss: () -> Unit,
    onSubscribe: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Premium Feature") },
        text = {
            Text(
                text = "Access to older puzzles is a premium feature. Subscribe now to unlock all past puzzles!",
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onSubscribe) {
                Text("Subscribe")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Maybe Later")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PremiumSubscriptionDialogPreview() {
    MaterialTheme {
        PremiumSubscriptionDialog(
            onDismiss = {},
            onSubscribe = {}
        )
    }
} 