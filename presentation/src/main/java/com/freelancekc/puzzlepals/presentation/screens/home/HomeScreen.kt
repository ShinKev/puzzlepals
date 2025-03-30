package com.freelancekc.puzzlepals.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.freelancekc.puzzlepals.presentation.R
import com.freelancekc.puzzlepals.presentation.components.CalendarButton
import com.freelancekc.puzzlepals.presentation.components.DateDisplay
import com.freelancekc.puzzlepals.presentation.components.DatePickerDialog
import com.freelancekc.puzzlepals.presentation.components.ImageCarousel
import com.freelancekc.puzzlepals.presentation.components.PlayButton
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val puzzles by viewModel.puzzles.collectAsStateWithLifecycle()
    var currentPage by remember { mutableIntStateOf(puzzles.size - 1) }

    // Offline images to be able to display something
    val sampleImages = remember {
        listOf(
            R.drawable.sample_puzzle1,
            R.drawable.sample_puzzle2,
            R.drawable.sample_puzzle3
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalendarButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            DateDisplay(
                date = selectedDate,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ImageCarousel(
                images = sampleImages, // should be puzzles.map { it.imageUrl },
                currentPage = currentPage,
                onPageChanged = { page ->
                    currentPage = page
                    selectedDate = puzzles[page].date
                },
                modifier = Modifier.weight(1f)
            )

            PlayButton(
                onClick = { /* TODO: Navigate to puzzle screen */ },
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    DatePickerDialog(
        showDialog = showDatePicker,
        selectedDate = selectedDate,
        onDismiss = { showDatePicker = false },
        onDateSelected = { date ->
            selectedDate = date
            // Calculate the page based on the selected date
            val today = Calendar.getInstance()
            val diffInMillis = today.timeInMillis - date.timeInMillis
            val diffInDays = (diffInMillis / (24 * 60 * 60 * 1000)).toInt()
            val newPage = (puzzles.size - 1 - diffInDays).coerceIn(0, puzzles.size - 1)
            if (newPage != currentPage) {
                currentPage = newPage
            }
        },
        onSubscribeToPremium = {
            // TODO: Navigate to premium subscription screen
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}