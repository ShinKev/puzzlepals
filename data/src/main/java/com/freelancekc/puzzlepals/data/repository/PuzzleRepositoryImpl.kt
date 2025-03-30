package com.freelancekc.puzzlepals.data.repository

import com.freelancekc.puzzlepals.data.remote.PuzzleRemoteDataSource
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PuzzleRepositoryImpl @Inject constructor(
    private val remoteDataSource: PuzzleRemoteDataSource
) : PuzzleRepository {
    override fun getRecentPuzzles(): Flow<List<Puzzle>> = remoteDataSource.getRecentPuzzles()

    override suspend fun getPuzzleById(id: String): Puzzle? {
        return remoteDataSource.getPuzzleById(id)
    }

    override fun getUserCreations(): Flow<List<Puzzle>> = remoteDataSource.getUserCreations()

    override suspend fun createPuzzle(puzzle: Puzzle) {
        remoteDataSource.createPuzzle(puzzle)
    }
} 