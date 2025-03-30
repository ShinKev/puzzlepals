package com.freelancekc.puzzlepals.presentation.screens.creation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.domain.usecase.CreatePuzzleUseCase
import com.freelancekc.puzzlepals.domain.usecase.GetUserCreationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreationViewModel @Inject constructor(
    private val getUserCreationsUseCase: GetUserCreationsUseCase,
    private val createPuzzleUseCase: CreatePuzzleUseCase
) : ViewModel() {

    private val _creations = MutableStateFlow<List<Puzzle>>(emptyList())
    val creations: StateFlow<List<Puzzle>> = _creations.asStateFlow()

    init {
        loadCreations()
    }

    private fun loadCreations() {
        viewModelScope.launch {
            getUserCreationsUseCase().collect { puzzleList ->
                _creations.value = puzzleList
            }
        }
    }

    fun handleImageSelected(uri: Uri, rows: Int, columns: Int) {
        viewModelScope.launch {
            val puzzle = Puzzle(
                id = UUID.randomUUID().toString(),
                imageUrl = uri.toString(),
                date = Calendar.getInstance(),
                rows = rows,
                columns = columns
            )
            createPuzzleUseCase(puzzle)
        }
    }
} 