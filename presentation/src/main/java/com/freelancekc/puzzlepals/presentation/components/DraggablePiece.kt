package com.freelancekc.puzzlepals.presentation.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.freelancekc.puzzlepals.domain.model.Coordinates
import com.freelancekc.puzzlepals.domain.model.PuzzlePiece
import com.freelancekc.puzzlepals.domain.model.Size
import com.freelancekc.puzzlepals.domain.util.SNAP_TOLERANCE
import com.freelancekc.puzzlepals.presentation.R
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun DraggablePiece(
    piece: PuzzlePiece,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isLocked by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val widthDp = with(density) { piece.pieceSize.width.toDp() }
    val heightDp = with(density) { piece.pieceSize.height.toDp() }

    // Calculate snap threshold based on piece diagonal length
    val pieceDiagonalLength = sqrt(
        piece.pieceSize.width.toDouble().pow(2) + piece.pieceSize.height.toDouble().pow(2)
    )
    val snapThreshold = pieceDiagonalLength * SNAP_TOLERANCE

    Surface(
        modifier = modifier
            .size(widthDp, heightDp)
            .zIndex(if (isLocked) 0f else 1f)  // Locked pieces go to the back
            .offset {
                // Calculate the current position
                val currentX = piece.startCoordinates.x + offsetX
                val currentY = piece.startCoordinates.y + offsetY

                // Calculate distance to win position
                val xDistanceLeft = abs(currentX - piece.winCoordinates.x)
                val yDistanceLeft = abs(currentY - piece.winCoordinates.y)
                val xyDistanceLeft = sqrt(
                    xDistanceLeft.toDouble().pow(2) + yDistanceLeft.toDouble().pow(2)
                )
                val shouldSnap = xyDistanceLeft < snapThreshold

                // If close enough, snap to the win position and lock
                if (shouldSnap && !isLocked) {
                    isLocked = true
                }

                // If locked, use the win position, otherwise use current position
                val finalX = if (isLocked) piece.winCoordinates.x else currentX.toInt()
                val finalY = if (isLocked) piece.winCoordinates.y else currentY.toInt()

                IntOffset(finalX, finalY)
            }
            .pointerInput(Unit) {
                if (!isLocked) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
            },
        tonalElevation = if (isLocked) 0.dp else 4.dp,
        shadowElevation = if (isLocked) 0.dp else 4.dp
    ) {
        Image(
            bitmap = piece.bitmap.asImageBitmap(),
            contentDescription = "Puzzle Piece ${piece.winCoordinates}",
            modifier = Modifier.size(widthDp, heightDp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DraggablePiecePreview() {
    val context = LocalContext.current
    val sampleBitmap = remember {
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.sample_puzzle1
        )
    }
    
    val piece = remember {
        PuzzlePiece(
            bitmap = sampleBitmap,
            pieceSize = Size(200, 200),
            winCoordinates = Coordinates(100, 100),
            startCoordinates = Coordinates(0, 0)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DraggablePiece(piece = piece)
    }
}
