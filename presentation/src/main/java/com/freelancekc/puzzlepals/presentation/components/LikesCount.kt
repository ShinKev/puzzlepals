package com.freelancekc.puzzlepals.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.puzzlepals.domain.util.NumberFormatUtils

@Composable
fun LikesCount(
    likesCount: Long,
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onLikeClick),
        color = Color.Black.copy(alpha = 0.6f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isLiked) "Liked" else "Not liked",
                tint = if (isLiked) Color.Red else Color.White,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = NumberFormatUtils.formatLikesCount(likesCount),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LikesCountPreview() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LikesCount(
            likesCount = 10_321_555,
            isLiked = true
        )
        LikesCount(
            likesCount = 1_321_555,
            isLiked = false
        )
        LikesCount(
            likesCount = 358_120,
            isLiked = true
        )
        LikesCount(likesCount = 10_543, isLiked = false)
        LikesCount(likesCount = 1_456, isLiked = false)
        LikesCount(likesCount = 531, isLiked = false)
    }
}
