package com.freelancekc.puzzlepals.presentation.screens.puzzle

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavBackStackEntry
import com.freelancekc.puzzlepals.presentation.R
import com.freelancekc.puzzlepals.domain.model.PuzzlePiece
import com.freelancekc.puzzlepals.presentation.components.DraggablePiece
import com.freelancekc.puzzlepals.presentation.utils.PuzzleGenerator
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun PuzzleScreen(
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier
) {
    val puzzleId = backStackEntry.arguments?.getString("puzzleId")
        ?: return // This should be used in the viewModel to fetch the puzzle details
    val context = LocalContext.current
    val density = LocalDensity.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    // Load bitmap image
    LaunchedEffect(Unit) {
        try {
            val drawable = context.getDrawable(R.drawable.sample_puzzle3)
            bitmap = (drawable as BitmapDrawable).bitmap
        } catch (e: Exception) {
            // Handle error
        }
    }

    // Generate puzzle pieces
    val puzzlePieces = remember(bitmap, imageSize) {
        if (imageSize == Size.Zero) return@remember emptyList()
        val loadedBitmap = bitmap ?: return@remember emptyList()
        PuzzleGenerator.generatePuzzlePieces(3, 3, loadedBitmap, imageSize, density)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.85f).fillMaxHeight(0.6f),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                PuzzleBackground(
                    bitmap = bitmap!!,
                    onSizeChanged = { imageSize = it }
                )
                if (imageSize != Size.Zero) {
                    PuzzlePieces(pieces = puzzlePieces)
                }
            }
        }
    }
}

@Composable
private fun PuzzleBackground(
    bitmap: Bitmap,
    onSizeChanged: (Size) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Puzzle Background",
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    onSizeChanged(size.toSize())
                },
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.5f))
        )
    }
}

@Composable
private fun PuzzlePieces(pieces: List<PuzzlePiece>) {
    pieces.forEach { piece ->
        DraggablePiece(piece = piece)
    }
}
