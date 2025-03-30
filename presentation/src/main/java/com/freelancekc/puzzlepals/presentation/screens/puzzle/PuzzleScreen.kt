package com.freelancekc.puzzlepals.presentation.screens.puzzle

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.freelancekc.puzzlepals.domain.model.PuzzlePiece
import com.freelancekc.puzzlepals.presentation.R
import com.freelancekc.puzzlepals.presentation.components.DraggablePiece
import com.freelancekc.puzzlepals.presentation.utils.PuzzleGenerator

@Composable
fun PuzzleScreen(
    viewModel: PuzzleViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var screenSize by remember { mutableStateOf(Size.Zero) }
    var imageSize by remember { mutableStateOf(Size.Zero) }

    val puzzle by viewModel.puzzle.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    // Load bitmap image
    LaunchedEffect(puzzle) {
        try {
            // Local image instead of URL since this is a prototype
            val drawable = context.getDrawable(R.drawable.sample_puzzle3)
            bitmap = (drawable as BitmapDrawable).bitmap
        } catch (e: Exception) {
            // Handle error
        }
    }

    // Generate puzzle pieces
    val puzzlePieces = remember(bitmap, imageSize, puzzle) {
        if (imageSize == Size.Zero) return@remember emptyList()
        val loadedBitmap = bitmap ?: return@remember emptyList()
        val puzzleRows = puzzle?.rows ?: return@remember emptyList()
        val puzzleColumns = puzzle?.columns ?: return@remember emptyList()
        PuzzleGenerator.generatePuzzlePieces(
            puzzleRows,
            puzzleColumns,
            loadedBitmap,
            imageSize,
            screenSize,
            density
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { screenSize = it.toSize() },
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(
                    text = error ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error
                )
            }
            puzzle != null -> {
                PuzzleContent(
                    puzzlePieces = puzzlePieces,
                    bitmap = bitmap,
                    imageSize = imageSize,
                    setImageSize = { imageSize = it }
                )
            }
        }
    }
}

@Composable
private fun PuzzleContent(
    puzzlePieces: List<PuzzlePiece>,
    bitmap: Bitmap?,
    imageSize: Size,
    setImageSize: (Size) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.85f).fillMaxHeight(0.6f),
            contentAlignment = Alignment.Center
        ) {
            if (bitmap != null) {
                PuzzleBackground(
                    bitmap = bitmap,
                    onSizeChanged = { setImageSize(it) }
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

@Preview(
    showBackground = true,
    widthDp = 1000,
    heightDp = 1000,
)
@Composable
fun PuzzleContentPreview() {
    val context = LocalContext.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) { 1000.dp.toPx() }
    val screenHeightPx = with(density) { 1000.dp.toPx() }
    val imageWidthPx = with(density) { 850.dp.toPx() }
    val imageHeightPx = with(density) { 600.dp.toPx() }

    val drawable = context.getDrawable(R.drawable.sample_puzzle3)
    val bitmap = (drawable as BitmapDrawable).bitmap

    val puzzlePieces = remember(bitmap) {
        val loadedBitmap = bitmap ?: return@remember emptyList()
        PuzzleGenerator.generatePuzzlePieces(
            rows = 3,
            columns = 3,
            bitmap = loadedBitmap,
            imageSize = Size(imageWidthPx, imageHeightPx),
            screenSize = Size(screenWidthPx, screenHeightPx),
            density = density
        )
    }

    PuzzleContent(
        puzzlePieces = puzzlePieces,
        bitmap = bitmap,
        imageSize = Size(imageWidthPx, imageHeightPx)
    )
}
