package com.freelancekc.puzzlepals.data.remote

import com.freelancekc.puzzlepals.domain.model.Puzzle
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Calendar

@OptIn(ExperimentalCoroutinesApi::class)
class PuzzleRemoteDataSourceImplTest {
    private lateinit var apiService: PuzzleApiService
    private lateinit var remoteDataSource: PuzzleRemoteDataSourceImpl
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        apiService = mockk()
        remoteDataSource = PuzzleRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getRecentPuzzles returns mocked puzzles for prototype`() = runTest(testDispatcher) {
        // When
        val result = remoteDataSource.getRecentPuzzles().take(1).single()

        // Then
        assertEquals(3, result.size)
        assertEquals("1", result[0].id)
        assertEquals("2", result[1].id)
        assertEquals("3", result[2].id)
    }

    @Test
    fun `getPuzzleById returns mocked puzzle when id matches`() = runTest(testDispatcher) {
        // Given
        val puzzleId = "1"

        // When
        val result = remoteDataSource.getPuzzleById(puzzleId)

        // Then
        assertEquals(puzzleId, result?.id)
        assertEquals("R.drawable.sample_puzzle1", result?.imageUrl)
        assertEquals(3, result?.rows)
        assertEquals(3, result?.columns)
    }

    @Test
    fun `getPuzzleById returns null for non-existent id`() = runTest(testDispatcher) {
        // Given
        val puzzleId = "non-existent"

        // When
        val result = remoteDataSource.getPuzzleById(puzzleId)

        // Then
        assertNull(result)
    }

    @Test
    fun `createPuzzle stores puzzle in memory and updates flow`() = runTest(testDispatcher) {
        // Given
        val puzzle = Puzzle(
            id = "new-puzzle",
            imageUrl = "R.drawable.new_puzzle",
            date = Calendar.getInstance(),
            rows = 4,
            columns = 4
        )

        // When
        val result = remoteDataSource.createPuzzle(puzzle)
        delay(100) // Give time for the flow to emit
        val userCreations = remoteDataSource.getUserCreations().take(1).single()

        // Then
        assertEquals(puzzle, result)
        assertEquals(1, userCreations.size)
        assertEquals(puzzle, userCreations[0])
    }

    @Test
    fun `getUserCreations returns all created puzzles`() = runTest(testDispatcher) {
        // Given
        val puzzle1 = Puzzle(
            id = "puzzle1",
            imageUrl = "R.drawable.puzzle1",
            date = Calendar.getInstance(),
            rows = 3,
            columns = 3
        )
        val puzzle2 = Puzzle(
            id = "puzzle2",
            imageUrl = "R.drawable.puzzle2",
            date = Calendar.getInstance(),
            rows = 4,
            columns = 4
        )

        // When
        remoteDataSource.createPuzzle(puzzle1)
        remoteDataSource.createPuzzle(puzzle2)
        delay(100) // Give time for the flow to emit
        val result = remoteDataSource.getUserCreations().take(1).single()

        // Then
        assertEquals(2, result.size)
        assertEquals(puzzle1, result[0])
        assertEquals(puzzle2, result[1])
    }
} 