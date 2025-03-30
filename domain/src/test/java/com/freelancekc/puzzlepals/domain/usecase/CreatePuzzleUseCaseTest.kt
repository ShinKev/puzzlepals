package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class CreatePuzzleUseCaseTest {

    private lateinit var puzzleRepository: PuzzleRepository
    private lateinit var createPuzzleUseCase: CreatePuzzleUseCase

    @Before
    fun setup() {
        puzzleRepository = mockk {
            coEvery { createPuzzle(any()) } just runs
        }
        createPuzzleUseCase = CreatePuzzleUseCase(puzzleRepository)
    }

    @Test
    fun `invoke calls repository createPuzzle with correct puzzle`() = runBlocking {
        // Given
        val puzzle = Puzzle(
            id = "1",
            imageUrl = "R.drawable.sample_puzzle1",
            date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) },
            rows = 3,
            columns = 3
        )

        // When
        createPuzzleUseCase(puzzle)

        // Then
        coVerify { puzzleRepository.createPuzzle(puzzle) }
    }
} 