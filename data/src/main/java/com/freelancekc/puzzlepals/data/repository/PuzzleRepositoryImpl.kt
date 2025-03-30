package com.freelancekc.puzzlepals.data.repository

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class PuzzleRepositoryImpl @Inject constructor() : PuzzleRepository {
    override fun getRecentPuzzles(): Flow<List<Puzzle>> = flow {
        // Mock data
        val puzzles = listOf(
            Puzzle(
                id = "1",
                imageUrl = "https://example.com/puzzle1.jpg",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) },
                rows = 5,
                columns = 5
            ),
            Puzzle(
                id = "2",
                imageUrl = "https://example.com/puzzle2.jpg",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) },
                rows = 5,
                columns = 5
            ),
            Puzzle(
                id = "3",
                imageUrl = "https://example.com/puzzle3.jpg",
                date = Calendar.getInstance(),
                rows = 5,
                columns = 5
            )
        )
        emit(puzzles)
    }
} 