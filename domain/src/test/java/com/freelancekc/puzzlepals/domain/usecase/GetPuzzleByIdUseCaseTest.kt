package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class GetPuzzleByIdUseCaseTest {
    private lateinit var repository: PuzzleRepository
    private lateinit var useCase: GetPuzzleByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetPuzzleByIdUseCase(repository)
    }

    @Test
    fun `when repository returns puzzle, useCase returns same puzzle`() = runBlocking {
        // Given
        val puzzleId = "test-id"
        val expectedPuzzle = Puzzle(
            id = puzzleId,
            imageUrl = "https://example.com/image.jpg",
            date = Calendar.getInstance(),
            rows = 3,
            columns = 3
        )
        coEvery { repository.getPuzzleById(puzzleId) } returns expectedPuzzle

        // When
        val result = useCase(puzzleId)

        // Then
        assertEquals(expectedPuzzle, result)
    }

    @Test
    fun `when repository returns null, useCase returns null`() = runBlocking {
        // Given
        val puzzleId = "non-existent-id"
        coEvery { repository.getPuzzleById(puzzleId) } returns null

        // When
        val result = useCase(puzzleId)

        // Then
        assertNull(result)
    }

    @Test
    fun `when repository throws exception, useCase propagates exception`() = runBlocking {
        // Given
        val puzzleId = "error-id"
        val expectedException = RuntimeException("Database error")
        coEvery { repository.getPuzzleById(puzzleId) } throws expectedException

        // When/Then
        try {
            useCase(puzzleId)
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException, e)
        }
    }
} 