package com.freelancekc.puzzlepals.presentation.screens.puzzle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.usecase.GetPuzzleByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PuzzleViewModel @Inject constructor(
    private val getPuzzleByIdUseCase: GetPuzzleByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _puzzle = MutableStateFlow<Puzzle?>(null)
    val puzzle: StateFlow<Puzzle?> = _puzzle.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        val puzzleId = savedStateHandle.get<String>("puzzleId")
        if (puzzleId != null) {
            loadPuzzle(puzzleId)
        }
    }

    private fun loadPuzzle(puzzleId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _puzzle.value = getPuzzleByIdUseCase(puzzleId)
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                _puzzle.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
} 