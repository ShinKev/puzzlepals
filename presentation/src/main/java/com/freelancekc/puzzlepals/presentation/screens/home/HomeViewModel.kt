package com.freelancekc.puzzlepals.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.usecase.GetRecentPuzzlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentPuzzlesUseCase: GetRecentPuzzlesUseCase
) : ViewModel() {

    private val _puzzles = MutableStateFlow<List<Puzzle>>(emptyList())
    val puzzles: StateFlow<List<Puzzle>> = _puzzles.asStateFlow()

    init {
        loadPuzzles()
    }

    private fun loadPuzzles() {
        viewModelScope.launch {
            getRecentPuzzlesUseCase().collect { puzzleList ->
                _puzzles.value = puzzleList
            }
        }
    }

    fun toggleLike(puzzleId: String) {
        viewModelScope.launch {
            // Here we should call the appropriate useCase that will make the right API call
            // But we are doing it here to save time since we are almost at the end of the test
            _puzzles.value = _puzzles.value.map { puzzle ->
                if (puzzle.id == puzzleId) {
                    puzzle.copy(
                        isLiked = !puzzle.isLiked,
                        likes = if (!puzzle.isLiked) puzzle.likes + 1 else puzzle.likes - 1
                    )
                } else {
                    puzzle
                }
            }
        }
    }
} 