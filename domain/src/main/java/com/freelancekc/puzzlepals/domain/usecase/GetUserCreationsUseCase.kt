package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserCreationsUseCase @Inject constructor(
    private val puzzleRepository: PuzzleRepository
) {
    operator fun invoke(): Flow<List<Puzzle>> {
        return puzzleRepository.getUserCreations()
    }
} 