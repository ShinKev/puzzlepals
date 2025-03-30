package com.freelancekc.puzzlepals.data.remote

import com.freelancekc.puzzlepals.domain.model.Puzzle
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Body
import java.util.Calendar
import java.util.Locale
import java.text.SimpleDateFormat

interface PuzzleApiService {
    @GET("puzzles/recent")
    suspend fun getRecentPuzzles(): List<PuzzleDto>

    @GET("puzzles/{id}")
    suspend fun getPuzzleById(@Path("id") id: String): PuzzleDto?

    @POST("puzzles")
    suspend fun createPuzzle(@Body puzzle: PuzzleDto): PuzzleDto
}

data class PuzzleDto(
    val id: String,
    val imageUrl: String,
    val date: String,
    val rows: Int,
    val columns: Int
) {
    fun toDomain(): Puzzle {
        return Puzzle(
            id = id,
            imageUrl = imageUrl,
            date = Calendar.getInstance().apply {
                timeInMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    .parse(date)?.time ?: System.currentTimeMillis()
            },
            rows = rows,
            columns = columns
        )
    }

    companion object {
        fun fromDomain(puzzle: Puzzle): PuzzleDto {
            return PuzzleDto(
                id = puzzle.id,
                imageUrl = puzzle.imageUrl,
                date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    .format(puzzle.date.time),
                rows = puzzle.rows,
                columns = puzzle.columns
            )
        }
    }
} 