package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.freelancekc.puzzlepals.domain.util.NumberFormatUtils

@Composable
fun LikesCount(
    likesCount: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Likes",
            tint = Color.White,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = NumberFormatUtils.formatLikesCount(likesCount),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}
