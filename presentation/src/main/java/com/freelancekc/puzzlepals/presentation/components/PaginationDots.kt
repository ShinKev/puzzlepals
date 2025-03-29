package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PaginationDots(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        repeat(pageCount) { page ->
            val color = if (page == currentPage) {
                androidx.compose.ui.graphics.Color.White
            } else {
                androidx.compose.ui.graphics.Color.White.copy(alpha = 0.3f)
            }
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(CircleShape)
                    .border(1.dp, androidx.compose.ui.graphics.Color.Black, CircleShape)
                    .background(color, CircleShape)
                    .size(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaginationDotsPreview() {
    PaginationDots(pageCount = 3, currentPage = 1)
} 