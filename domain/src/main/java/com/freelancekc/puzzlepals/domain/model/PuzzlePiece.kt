package com.freelancekc.puzzlepals.domain.model

import android.graphics.Bitmap

data class PuzzlePiece(
    val bitmap: Bitmap,
    val pieceSize: Size,
    val winCoordinates: Coordinates,
    val startCoordinates: Coordinates = Coordinates()
)
