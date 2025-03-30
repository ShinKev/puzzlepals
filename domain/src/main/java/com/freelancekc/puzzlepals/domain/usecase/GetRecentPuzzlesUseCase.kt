package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentPuzzlesUseCase @Inject constructor(
    private val repository: PuzzleRepository
) {
    operator fun invoke(): Flow<List<Puzzle>> {
        return repository.getRecentPuzzles()
    }
} 