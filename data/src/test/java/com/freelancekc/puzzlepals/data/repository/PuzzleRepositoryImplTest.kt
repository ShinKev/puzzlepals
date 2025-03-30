package com.freelancekc.puzzlepals.data.repository

import com.freelancekc.puzzlepals.data.remote.PuzzleRemoteDataSource
import com.freelancekc.puzzlepals.domain.model.Puzzle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class PuzzleRepositoryImplTest {
    private lateinit var remoteDataSource: PuzzleRemoteDataSource
    private lateinit var repository: PuzzleRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk()
        repository = PuzzleRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getRecentPuzzles returns puzzles from remote data source`() = runBlocking {
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
        every { remoteDataSource.getRecentPuzzles() } returns flowOf(mockPuzzles)

        // When
        val result = repository.getRecentPuzzles().single()

        // Then
        assertEquals(mockPuzzles, result)
        verify { remoteDataSource.getRecentPuzzles() }
    }

    @Test
    fun `getPuzzleById returns puzzle from remote data source when found`() = runBlocking {
        // Given
        val puzzleId = "1"
        val mockPuzzle = Puzzle(
            id = puzzleId,
            imageUrl = "R.drawable.sample_puzzle1",
            date = Calendar.getInstance(),
            rows = 3,
            columns = 3
        )
        coEvery { remoteDataSource.getPuzzleById(puzzleId) } returns mockPuzzle

        // When
        val result = repository.getPuzzleById(puzzleId)

        // Then
        assertEquals(mockPuzzle, result)
        coVerify { remoteDataSource.getPuzzleById(puzzleId) }
    }

    @Test
    fun `getPuzzleById returns null when remote data source returns null`() = runBlocking {
        // Given
        val puzzleId = "non-existent"
        coEvery { remoteDataSource.getPuzzleById(puzzleId) } returns null

        // When
        val result = repository.getPuzzleById(puzzleId)

        // Then
        assertNull(result)
        coVerify { remoteDataSource.getPuzzleById(puzzleId) }
    }

    @Test
    fun `getUserCreations returns puzzles from remote data source`() = runBlocking {
        // Given
        val mockPuzzles = listOf(
            Puzzle(
                id = "1",
                imageUrl = "R.drawable.sample_puzzle1",
                date = Calendar.getInstance(),
                rows = 3,
                columns = 3
            )
        )
        every { remoteDataSource.getUserCreations() } returns flowOf(mockPuzzles)

        // When
        val result = repository.getUserCreations().single()

        // Then
        assertEquals(mockPuzzles, result)
        verify { remoteDataSource.getUserCreations() }
    }

    @Test
    fun `createPuzzle delegates to remote data source`() = runBlocking {
        // Given
        val puzzle = Puzzle(
            id = "1",
            imageUrl = "R.drawable.sample_puzzle1",
            date = Calendar.getInstance(),
            rows = 3,
            columns = 3
        )
        coEvery { remoteDataSource.createPuzzle(puzzle) } returns puzzle

        // When
        repository.createPuzzle(puzzle)

        // Then
        coVerify { remoteDataSource.createPuzzle(puzzle) }
    }
} 