package com.freelancekc.puzzlepals.domain.model

import java.util.Calendar

data class Puzzle(
    val id: String,
    val imageUrl: String,
    val date: Calendar,
    val rows: Int,
    val columns: Int,
    val likes: Long = 0
) 