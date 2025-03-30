package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class GetUserCreationsUseCaseTest {

    private lateinit var puzzleRepository: PuzzleRepository
    private lateinit var getUserCreationsUseCase: GetUserCreationsUseCase

    @Before
    fun setup() {
        puzzleRepository = mockk()
        getUserCreationsUseCase = GetUserCreationsUseCase(puzzleRepository)
    }

    @Test
    fun `invoke returns list of puzzles from repository`() = runBlocking {
        // Given
        val mockPuzzles = listOf(
            Puzzle(
                id = "1",
                imageUrl = "R.drawable.sample_puzzle1",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) },
                rows = 3,
                columns = 3
            ),
            Puzzle(
                id = "2",
                imageUrl = "R.drawable.sample_puzzle2",
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) },
                rows = 3,
                columns = 3
            )
        )
        coEvery { puzzleRepository.getUserCreations() } returns flowOf(mockPuzzles)

        // When
        val result = getUserCreationsUseCase().single()

        // Then
        assertEquals(mockPuzzles, result)
    }
} 