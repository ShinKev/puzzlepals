package com.freelancekc.puzzlepals.presentation.utils

import android.graphics.Bitmap
import com.freelancekc.puzzlepals.domain.model.Coordinates
import com.freelancekc.puzzlepals.domain.model.PuzzlePiece
import com.freelancekc.puzzlepals.domain.model.Size

object PuzzleImageSplitter {
    fun splitImage(bitmap: Bitmap, rows: Int, columns: Int): List<PuzzlePiece> {
        val pieces = mutableListOf<PuzzlePiece>()
        val pieceWidth = bitmap.width / columns
        val pieceHeight = bitmap.height / rows

        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val xPosition = col * pieceWidth
                val yPosition = row * pieceHeight
                
                val pieceBitmap = Bitmap.createBitmap(
                    bitmap,
                    xPosition,
                    yPosition,
                    pieceWidth,
                    pieceHeight
                )

                pieces.add(
                    PuzzlePiece(
                        bitmap = pieceBitmap,
                        pieceSize = Size(pieceWidth, pieceHeight),
                        winCoordinates = Coordinates(
                            x = xPosition - (bitmap.width - pieceWidth)/2,
                            y = yPosition - (bitmap.height - pieceHeight)/2
                        )
                    )
                )
            }
        }

        return pieces
    }
}
