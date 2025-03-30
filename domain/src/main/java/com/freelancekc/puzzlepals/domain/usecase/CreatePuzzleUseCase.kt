package com.freelancekc.puzzlepals.domain.usecase

import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.repository.PuzzleRepository
import javax.inject.Inject

class CreatePuzzleUseCase @Inject constructor(
    private val puzzleRepository: PuzzleRepository
) {
    suspend operator fun invoke(puzzle: Puzzle) {
        puzzleRepository.createPuzzle(puzzle)
    }
} 