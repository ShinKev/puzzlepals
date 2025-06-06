package com.freelancekc.puzzlepals.presentation.screens.home

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.util.isOlderThanThreeDays
import com.freelancekc.puzzlepals.domain.util.setToMidnight
import com.freelancekc.puzzlepals.presentation.R
import com.freelancekc.puzzlepals.presentation.components.CalendarButton
import com.freelancekc.puzzlepals.presentation.components.DateDisplay
import com.freelancekc.puzzlepals.presentation.components.DatePickerDialog
import com.freelancekc.puzzlepals.presentation.components.ImageCarousel
import com.freelancekc.puzzlepals.presentation.components.PlayButton
import com.freelancekc.puzzlepals.presentation.components.PremiumSubscriptionDialog
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPuzzle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showPremiumDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    val puzzles by viewModel.puzzles.collectAsStateWithLifecycle()
    var currentPage by remember { mutableIntStateOf(puzzles.size - 1) }

    HomeContent(
        puzzles = puzzles,
        selectedDate = selectedDate,
        currentPage = currentPage,
        onNavigateToPuzzle = onNavigateToPuzzle,
        onLikeClick = { puzzleId -> viewModel.toggleLike(puzzleId) },
        setShowDatePicker = { showDatePicker = it },
        setShowPremiumDialog = { showPremiumDialog = it },
        setSelectedDate = { selectedDate = it },
        setCurrentPage = { currentPage = it },
        modifier = modifier
    )

    DatePickerDialog(
        showDialog = showDatePicker,
        selectedDate = selectedDate,
        onDismiss = { showDatePicker = false },
        onDateSelected = { date ->
            if (date.isOlderThanThreeDays()) {
                showPremiumDialog = true
            } else {
                selectedDate = date
                val newPage = calculatePageForDate(date, puzzles.size)
                if (newPage != currentPage) {
                    currentPage = newPage
                }
            }
            showDatePicker = false
        }
    )

    if (showPremiumDialog) {
        PremiumSubscriptionDialog(
            onDismiss = { showPremiumDialog = false },
            onSubscribe = {
                Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
                showPremiumDialog = false
            }
        )
    }
}

@Composable
private fun HomeContent(
    puzzles: List<Puzzle>,
    selectedDate: Calendar,
    currentPage: Int,
    onNavigateToPuzzle: (String) -> Unit = {},
    onLikeClick: (String) -> Unit = {},
    setShowDatePicker: (Boolean) -> Unit = {},
    setShowPremiumDialog: (Boolean) -> Unit = {},
    setSelectedDate: (Calendar) -> Unit = {},
    setCurrentPage: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CalendarButton(
                    onClick = { setShowDatePicker(true) },
                    modifier = Modifier.padding(end = 24.dp)
                )
            }

            DateDisplay(
                date = selectedDate,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ImageCarousel(
                puzzles = puzzles,
                currentPage = currentPage,
                onPageChanged = { page ->
                    setCurrentPage(page)
                    setSelectedDate(puzzles[page].date)
                },
                onLikeClick = onLikeClick,
                modifier = Modifier.weight(1f)
            )

            PlayButton(
                onClick = {
                    // Check if the selected puzzle is from today
                    val today = Calendar.getInstance()
                    today.setToMidnight()

                    val selectedPuzzleDate = puzzles[currentPage].date.clone() as Calendar
                    selectedPuzzleDate.setToMidnight()

                    if (selectedPuzzleDate.before(today)) {
                        setShowPremiumDialog(true)
                    } else {
                        onNavigateToPuzzle(puzzles[currentPage].id)
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

private fun calculatePageForDate(date: Calendar, puzzlesSize: Int): Int {
    val today = Calendar.getInstance()
    val diffInMillis = today.timeInMillis - date.timeInMillis
    val diffInDays = (diffInMillis / (24 * 60 * 60 * 1000)).toInt()
    return (puzzlesSize - 1 - diffInDays).coerceIn(0, puzzlesSize - 1)
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    val samplePuzzles = remember {
        listOf(
            Puzzle(
                id = "1",
                imageUrl = R.drawable.sample_puzzle1.toString(),
                date = Calendar.getInstance(),
                rows = 3,
                columns = 3,
                likes = 42,
                isLiked = true
            ),
            Puzzle(
                id = "2",
                imageUrl = R.drawable.sample_puzzle2.toString(),
                date = Calendar.getInstance(),
                rows = 3,
                columns = 3,
                likes = 15,
                isLiked = false
            ),
            Puzzle(
                id = "3",
                imageUrl = R.drawable.sample_puzzle3.toString(),
                date = Calendar.getInstance(),
                rows = 3,
                columns = 3,
                likes = 7,
                isLiked = true
            )
        )
    }

    MaterialTheme {
        HomeContent(
            puzzles = samplePuzzles,
            selectedDate = Calendar.getInstance(),
            currentPage = 1,
            onLikeClick = {}
        )
    }
}