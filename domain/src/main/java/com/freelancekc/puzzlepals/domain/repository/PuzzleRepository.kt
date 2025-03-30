package com.freelancekc.puzzlepals.domain.repository

import com.freelancekc.puzzlepals.domain.model.Puzzle
import kotlinx.coroutines.flow.Flow

interface PuzzleRepository {
    fun getRecentPuzzles(): Flow<List<Puzzle>>
    suspend fun getPuzzleById(id: String): Puzzle?
} 