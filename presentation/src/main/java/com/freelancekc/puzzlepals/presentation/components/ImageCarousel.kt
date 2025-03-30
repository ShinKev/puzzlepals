package com.freelancekc.puzzlepals.presentation.components

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.presentation.R
import com.freelancekc.puzzlepals.presentation.utils.getBitmapFromUrl

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    puzzles: List<Puzzle>,
    currentPage: Int,
    onPageChanged: (Int) -> Unit,
    onLikeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialPage = currentPage) {
        puzzles.size
    }
    var bitmapList by remember { mutableStateOf<List<Bitmap?>>(emptyList()) }

    val context = LocalContext.current

    LaunchedEffect(puzzles) {
        bitmapList = puzzles.map { puzzle ->
            getBitmapFromUrl(context = context, imageUrl = puzzle.imageUrl)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        onPageChanged(pagerState.currentPage)
    }

    LaunchedEffect(currentPage) {
        if (currentPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }


    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (bitmapList.isNotEmpty()) {
            PuzzlePager(
                pagerState = pagerState,
                puzzles = puzzles,
                bitmapList = bitmapList,
                onLikeClick = onLikeClick
            )
        } else {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.PuzzlePager(
    pagerState: PagerState,
    puzzles: List<Puzzle>,
    bitmapList: List<Bitmap?> = emptyList(),
    onLikeClick: (String) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .clip(MaterialTheme.shapes.large),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                bitmapList[page]?.let { bitmap ->
                    // It would have been better to user AsyncImage directly
                    // but since we want to use the local images for the prototype,
                    // we are using getBitmapFromUrl to get our bitmap from our fake url
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Puzzle image ${page + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                LikesCount(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .padding(12.dp),
                    likesCount = puzzles[page].likes,
                    isLiked = puzzles[page].isLiked,
                    onLikeClick = { onLikeClick(puzzles[page].id) }
                )
            }
        }
    }

    PaginationDots(
        pageCount = puzzles.size,
        currentPage = pagerState.currentPage,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun PuzzlePagerPreview() {
    val context = LocalContext.current
    val puzzles = remember {
        listOf(
            Puzzle(
                id = "1",
                imageUrl = R.drawable.sample_puzzle1.toString(),
                date = java.util.Calendar.getInstance(),
                rows = 3,
                columns = 3,
                likes = 1_321_555,
                isLiked = true
            ),
            Puzzle(
                id = "2",
                imageUrl = R.drawable.sample_puzzle2.toString(),
                date = java.util.Calendar.getInstance(),
                rows = 3,
                columns = 3,
                likes = 358_120,
                isLiked = false
            ),
            Puzzle(
                id = "3",
                imageUrl = R.drawable.sample_puzzle3.toString(),
                date = java.util.Calendar.getInstance(),
                rows = 3,
                columns = 3,
                likes = 10_543,
                isLiked = true
            )
        )
    }

    val bitmapList = listOf(
        context.getDrawable(R.drawable.sample_puzzle1),
        context.getDrawable(R.drawable.sample_puzzle2),
        context.getDrawable(R.drawable.sample_puzzle3)
    ).map { (it as BitmapDrawable).bitmap }

    val pagerState = rememberPagerState(initialPage = 0) {
        puzzles.size
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PuzzlePager(
            pagerState = pagerState,
            puzzles = puzzles,
            bitmapList = bitmapList,
            onLikeClick = {}
        )
    }
} 