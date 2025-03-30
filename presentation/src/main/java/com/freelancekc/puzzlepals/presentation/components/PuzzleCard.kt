package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.freelancekc.puzzlepals.domain.model.Puzzle

@Composable
fun PuzzleCard(
    puzzle: Puzzle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = puzzle.imageUrl,
                contentDescription = "Puzzle image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                DimensionsText(
                    rows = puzzle.rows,
                    columns = puzzle.columns
                )
            }
        }
    }
}

@Composable
private fun DimensionsText(
    rows: Int,
    columns: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        color = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "$rows x $columns",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PuzzleCardPreview() {
    MaterialTheme {
        PuzzleCard(
            puzzle = Puzzle(
                id = "1",
                imageUrl = "https://example.com/puzzle1.jpg",
                date = java.util.Calendar.getInstance(),
                rows = 3,
                columns = 3
            ),
            onClick = {}
        )
    }
} 