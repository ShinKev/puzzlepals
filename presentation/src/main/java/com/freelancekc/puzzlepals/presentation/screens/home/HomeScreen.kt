package com.freelancekc.puzzlepals.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freelancekc.puzzlepals.presentation.R
import java.util.Calendar
import java.util.Locale

private fun Calendar.toEpochMillis(): Long {
    return this.timeInMillis
}

private fun Long.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = this@toCalendar
    }
}

@Composable
private fun CalendarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarToday,
            contentDescription = "Calendar"
        )
    }
}

@Composable
private fun DateDisplay(
    date: Calendar,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.get(Calendar.DAY_OF_MONTH).toString(),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())?.let {
            Text(
                text = it.uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PaginationDots(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color = if (currentPage == iteration) {
                    androidx.compose.ui.graphics.Color.White
                } else {
                    androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f)
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .border(1.dp, androidx.compose.ui.graphics.Color.Black, CircleShape)
                        .background(color, CircleShape)
                        .size(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageCarousel(
    images: List<Int>,
    currentPage: Int,
    onPageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        pageCount = { images.size },
        initialPage = images.size - 1
    )
    
    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    LaunchedEffect(currentPage) {
        if (currentPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = painterResource(id = images[page]),
                    contentDescription = "Daily Puzzle",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        PaginationDots(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    showDialog: Boolean,
    selectedDate: Calendar,
    onDismiss: () -> Unit,
    onDateSelected: (Calendar) -> Unit
) {
    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.toEpochMillis()
        )

        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(millis.toCalendar())
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
                title = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var currentPage by remember { mutableStateOf(2) } // Start at the last page

    val images = listOf(
        R.drawable.sample_puzzle1,
        R.drawable.sample_puzzle2,
        R.drawable.sample_puzzle3
    )

    // Update date when page changes
    LaunchedEffect(currentPage) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -(images.size - 1 - currentPage))
        selectedDate = calendar
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    CalendarButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            DateDisplay(date = selectedDate)
            ImageCarousel(
                images = images,
                currentPage = currentPage,
                onPageChanged = { currentPage = it }
            )
        }
    }

    DatePickerDialog(
        showDialog = showDatePicker,
        selectedDate = selectedDate,
        onDismiss = { showDatePicker = false },
        onDateSelected = { 
            selectedDate = it
            // Calculate the page based on the selected date
            val today = Calendar.getInstance()
            val diffInMillis = today.timeInMillis - it.timeInMillis
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

@Preview(showBackground = true)
@Composable
fun PaginationDotsPreview() {
    PaginationDots(3, 1)
}