package com.freelancekc.puzzlepals.presentation.screens.puzzle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry

@Composable
fun PuzzleScreen(
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier
) {
    val puzzleId = backStackEntry.arguments?.getString("puzzleId") ?: return

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Puzzle ID: $puzzleId")
        }
    }
} 