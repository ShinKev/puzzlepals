package com.freelancekc.puzzlepals.presentation.utils

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.freelancekc.puzzlepals.domain.model.Coordinates
import com.freelancekc.puzzlepals.domain.model.PuzzlePiece
import kotlin.random.Random

object PuzzleGenerator {
    fun generatePuzzlePieces(
        rows: Int,
        columns: Int,
        bitmap: Bitmap,
        imageSize: Size,
        screenSize: Size,
        density: Density
    ): List<PuzzlePiece> {
        val croppedBitmap = ImageProcessor.scaleAndCropBitmap(bitmap, imageSize)
        val pieces = PuzzleImageSplitter.splitImage(croppedBitmap, rows, columns)
        return shufflePieces(pieces, screenSize, density)
    }

    private fun shufflePieces(
        pieces: List<PuzzlePiece>,
        screenSize: Size,
        density: Density
    ): List<PuzzlePiece> {
        val screenWidth = screenSize.width.toInt()
        val screenHeight = screenSize.height.toInt()

        val padding = with(density) { 10.dp.toPx().toInt() }

        return pieces.map { piece ->
            val newX = Random.nextInt(padding, screenWidth - piece.pieceSize.width - padding)
            val newY = Random.nextInt(padding, screenHeight - piece.pieceSize.height - padding)

            piece.copy(
                startCoordinates = Coordinates(
                    x = newX - (screenWidth - piece.pieceSize.width) / 2,
                    y = newY - (screenHeight - piece.pieceSize.height) / 2
                )
            )
        }
    }
}