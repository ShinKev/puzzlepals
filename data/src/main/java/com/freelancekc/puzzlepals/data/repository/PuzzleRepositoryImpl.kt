package com.freelancekc.puzzlepals.data.repository

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject

class PuzzleRepositoryImpl @Inject constructor() : PuzzleRepository {
    private val mockedPuzzle1 = Puzzle(
        id = "1",
        imageUrl = "https://example.com/puzzle1.jpg",
        date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) },
        rows = 3,
        columns = 3
    )

    private val mockedPuzzle2 = Puzzle(
        id = "2",
        imageUrl = "https://example.com/puzzle2.jpg",
        date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) },
        rows = 3,
        columns = 3
    )

    private val mockedPuzzle3 = Puzzle(
        id = "3",
        imageUrl = "https://example.com/puzzle3.jpg",
        date = Calendar.getInstance(),
        rows = 3,
        columns = 3
    )

    private val userCreations = mutableListOf(mockedPuzzle1, mockedPuzzle2)

    override fun getRecentPuzzles(): Flow<List<Puzzle>> = flow {
        // Mock data
        val puzzles = listOf(mockedPuzzle1, mockedPuzzle2, mockedPuzzle3)
        emit(puzzles)
    }

    override suspend fun getPuzzleById(id: String): Puzzle? {
        // Mock data - in a real app, this would fetch from a database or API
        return when (id) {
            "1" -> mockedPuzzle1
            "2" -> mockedPuzzle2
            "3" -> mockedPuzzle3
            else -> null
        }
    }

    override fun getUserCreations(): Flow<List<Puzzle>> = flow {
        // Mock data - in a real app, this would fetch from a database or API
        emit(userCreations)
    }

    override suspend fun createPuzzle(puzzle: Puzzle) {
        // Mock data - in a real app, this would save to a database or API
        userCreations.add(0, puzzle)
    }
} 