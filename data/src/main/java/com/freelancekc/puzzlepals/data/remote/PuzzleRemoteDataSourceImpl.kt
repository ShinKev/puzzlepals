package com.freelancekc.puzzlepals.data.remote

import com.freelancekc.puzzlepals.domain.model.Puzzle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PuzzleRemoteDataSourceImpl @Inject constructor(
    private val apiService: PuzzleApiService
) : PuzzleRemoteDataSource {
    private val userCreations = mutableMapOf<String, Puzzle>()
    // replay = 1 in order to emit the cached list when we leave and go back to the screen
    private val _userCreationsFlow = MutableSharedFlow<List<Puzzle>>(replay = 1)
    private val userCreationsFlow = _userCreationsFlow.asSharedFlow()

    // Mock data for prototype
    private val mockedPuzzle1 = Puzzle(
        id = "1",
        imageUrl = "R.drawable.sample_puzzle1",
        date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) },
        rows = 3,
        columns = 3,
        likes = 1_321_555
    )

    private val mockedPuzzle2 = Puzzle(
        id = "2",
        imageUrl = "R.drawable.sample_puzzle2",
        date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) },
        rows = 3,
        columns = 3,
        likes = 358_120
    )

    private val mockedPuzzle3 = Puzzle(
        id = "3",
        imageUrl = "R.drawable.sample_puzzle3",
        date = Calendar.getInstance(),
        rows = 3,
        columns = 3,
        likes = 10_544
    )

    private val recentPuzzles = listOf(mockedPuzzle1, mockedPuzzle2, mockedPuzzle3)

    override fun getRecentPuzzles(): Flow<List<Puzzle>> = flow {
        // Mock data for prototype
        emit(recentPuzzles)

        /* Real API implementation:
        try {
            // Make API call to fetch recent puzzles
            val puzzles = apiService.getRecentPuzzles()
                .map { it.toDomain() } // Convert DTO to domain model
            emit(puzzles)
        } catch (e: Exception) {
            // Handle different types of errors appropriately
            when (e) {
                is HttpException -> {
                    // Handle HTTP errors (4xx, 5xx)
                    when (e.code()) {
                        401 -> throw UnauthorizedException()
                        403 -> throw ForbiddenException()
                        404 -> throw NotFoundException()
                        else -> throw NetworkException("Failed to fetch recent puzzles")
                    }
                }
                is IOException -> throw NetworkException("Network error occurred")
                else -> throw UnknownException("An unexpected error occurred")
            }
        }
        */
    }

    override suspend fun getPuzzleById(id: String): Puzzle? {
        // Mock data for prototype
        return when (id) {
            "1" -> mockedPuzzle1
            "2" -> mockedPuzzle2
            "3" -> mockedPuzzle3
            else -> userCreations[id]
        }

        /* Real API implementation:
        return try {
            // Make API call to fetch puzzle by ID
            apiService.getPuzzleById(id)?.toDomain()
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> throw UnauthorizedException()
                        403 -> throw ForbiddenException()
                        404 -> throw NotFoundException()
                        else -> throw NetworkException("Failed to fetch puzzle")
                    }
                }
                is IOException -> throw NetworkException("Network error occurred")
                else -> throw UnknownException("An unexpected error occurred")
            }
        }
        */
    }

    override suspend fun createPuzzle(puzzle: Puzzle): Puzzle {
        // Store in memory for prototype
        userCreations[puzzle.id] = puzzle
        _userCreationsFlow.emit(userCreations.values.toList())
        return puzzle

        /* Real API implementation:
        return try {
            // Convert domain model to DTO and make API call
            val puzzleDto = PuzzleDto.fromDomain(puzzle)
            val createdPuzzle = apiService.createPuzzle(puzzleDto).toDomain()
            
            // Update local cache
            userCreations[createdPuzzle.id] = createdPuzzle
            _userCreationsFlow.emit(userCreations.values.toList())
            
            createdPuzzle
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> throw UnauthorizedException()
                        403 -> throw ForbiddenException()
                        400 -> throw BadRequestException("Invalid puzzle data")
                        else -> throw NetworkException("Failed to create puzzle")
                    }
                }
                is IOException -> throw NetworkException("Network error occurred")
                else -> throw UnknownException("An unexpected error occurred")
            }
        }
        */
    }

    override fun getUserCreations(): Flow<List<Puzzle>> = userCreationsFlow
} 