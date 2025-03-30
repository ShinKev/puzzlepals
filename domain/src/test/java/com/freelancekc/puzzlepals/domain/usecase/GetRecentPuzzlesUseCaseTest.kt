package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class GetRecentPuzzlesUseCaseTest {
    private lateinit var repository: PuzzleRepository
    private lateinit var useCase: GetRecentPuzzlesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetRecentPuzzlesUseCase(repository)
    }

    @Test
    fun `when repository returns puzzles, useCase returns same puzzles`() = runBlocking {
        // Given
        val expectedPuzzles = listOf(
            Puzzle(
                id = "1",
                imageUrl = "https://example.com/image1.jpg",
                date = Calendar.getInstance(),
                rows = 3,
                columns = 3
            ),
            Puzzle(
                id = "2",
                imageUrl = "https://example.com/image2.jpg",
                date = Calendar.getInstance(),
                rows = 4,
                columns = 4
            )
        )
        every { repository.getRecentPuzzles() } returns flowOf(expectedPuzzles)

        // When
        val result = useCase().single()

        // Then
        assertEquals(expectedPuzzles, result)
    }

    @Test
    fun `when repository returns empty list, useCase returns empty list`() = runBlocking {
        // Given
        every { repository.getRecentPuzzles() } returns flowOf(emptyList())

        // When
        val result = useCase().single()

        // Then
        assertEquals(emptyList<Puzzle>(), result)
    }

    @Test
    fun `when repository throws exception, useCase propagates exception`() = runBlocking {
        // Given
        val expectedException = RuntimeException("Database error")
        every { repository.getRecentPuzzles() } throws expectedException

        // When/Then
        try {
            useCase().single()
            throw AssertionError("Expected exception to be thrown")
        } catch (e: RuntimeException) {
            assertEquals(expectedException, e)
        }
    }
} 