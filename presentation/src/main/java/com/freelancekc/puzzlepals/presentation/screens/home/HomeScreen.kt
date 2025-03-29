package com.freelancekc.puzzlepals.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.puzzlepals.presentation.R
import com.freelancekc.puzzlepals.presentation.components.CalendarButton
import com.freelancekc.puzzlepals.presentation.components.DateDisplay
import com.freelancekc.puzzlepals.presentation.components.ImageCarousel
import com.freelancekc.puzzlepals.presentation.components.DatePickerDialog
import java.util.Calendar

@Composable
fun HomeScreen() {
    val images = remember {
        listOf(
            R.drawable.sample_puzzle1,
            R.drawable.sample_puzzle2,
            R.drawable.sample_puzzle3
        )
    }

    // Initialize with the last page (today's date)
    var currentPage by remember { mutableIntStateOf(images.size - 1) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
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
                images = images,
                currentPage = currentPage,
                onPageChanged = { page ->
                    currentPage = page
                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DAY_OF_MONTH, -(images.size - 1 - page))
                    selectedDate = calendar
                }
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
            val newPage = (images.size - 1 - diffInDays).coerceIn(0, images.size - 1)
            if (newPage != currentPage) {
                currentPage = newPage
            }
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