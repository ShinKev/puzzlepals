package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import javax.inject.Inject

class GetPuzzleByIdUseCase @Inject constructor(
    private val repository: PuzzleRepository
) {
    suspend operator fun invoke(id: String): Puzzle? {
        return repository.getPuzzleById(id)
    }
} 