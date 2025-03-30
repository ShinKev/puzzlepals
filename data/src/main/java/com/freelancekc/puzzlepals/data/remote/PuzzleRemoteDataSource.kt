package com.freelancekc.puzzlepals.data.remote

import com.freelancekc.puzzlepals.domain.model.Puzzle
import kotlinx.coroutines.flow.Flow

interface PuzzleRemoteDataSource {
    fun getRecentPuzzles(): Flow<List<Puzzle>>
    suspend fun getPuzzleById(id: String): Puzzle?
    suspend fun createPuzzle(puzzle: Puzzle): Puzzle
    fun getUserCreations(): Flow<List<Puzzle>>
} 