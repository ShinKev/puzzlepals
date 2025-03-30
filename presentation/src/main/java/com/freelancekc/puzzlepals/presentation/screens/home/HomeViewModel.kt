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
} 