package com.freelancekc.puzzlepals.presentation.screens.creation

import android.Manifest
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.freelancekc.puzzlepals.domain.model.Puzzle
import com.freelancekc.puzzlepals.presentation.components.DimensionsDialog
import com.freelancekc.puzzlepals.presentation.components.PuzzleCard
import com.freelancekc.puzzlepals.presentation.utils.rememberCameraLauncherWithPermission
import com.freelancekc.puzzlepals.presentation.utils.rememberGalleryLauncher
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreationScreen(
    viewModel: CreationViewModel = hiltViewModel(),
    onNavigateToPuzzle: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val creations by viewModel.creations.collectAsStateWithLifecycle()
    var showDimensionsDialog by remember { mutableStateOf(false) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var rows by remember { mutableStateOf(2f) }
    var columns by remember { mutableStateOf(2f) }

    val galleryLauncher = rememberGalleryLauncher { uri ->
        selectedUri = uri
        showDimensionsDialog = true
    }

    val cameraLauncher = rememberCameraLauncherWithPermission { uri ->
        selectedUri = uri
        showDimensionsDialog = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Creations",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            CreationFloatingActionButtons(
                galleryLauncher = galleryLauncher,
                cameraLauncher = cameraLauncher
            )
        }
    ) { paddingValues ->
        CreationContent(
            creations = creations,
            paddingValues = paddingValues,
            modifier = modifier,
            onNavigateToPuzzle = onNavigateToPuzzle
        )
    }

    if (showDimensionsDialog) {
        DimensionsDialog(
            rows = rows,
            columns = columns,
            onRowsChange = { rows = it },
            onColumnsChange = { columns = it },
            onConfirm = {
                selectedUri?.let { uri ->
                    viewModel.handleImageSelected(uri, rows.toInt(), columns.toInt())
                }
                showDimensionsDialog = false
                selectedUri = null
                rows = 2f
                columns = 2f
            },
            onDismiss = {
                showDimensionsDialog = false
                selectedUri = null
                rows = 2f
                columns = 2f
            }
        )
    }
}

@Composable
private fun CreationContent(
    creations: List<Puzzle>,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    onNavigateToPuzzle: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (creations.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No creations yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(creations) { puzzle ->
                    PuzzleCard(
                        puzzle = puzzle,
                        onClick = { onNavigateToPuzzle(puzzle.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CreationFloatingActionButtons(
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>? = null,
    cameraLauncher: ActivityResultLauncher<String>? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = { galleryLauncher?.launch("image/*") },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Default.PhotoLibrary,
                contentDescription = "Choose from gallery"
            )
        }
        FloatingActionButton(
            onClick = { cameraLauncher?.launch(Manifest.permission.CAMERA) },
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Default.AddAPhoto,
                contentDescription = "Take a photo"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreationContentNoDataPreview() {
    MaterialTheme {
        CreationContent(creations = emptyList())
    }
}

@Preview(showBackground = true)
@Composable
fun CreationContentPreview() {
    // Mock data for prototype
    val mockedPuzzle1 = Puzzle(
        id = "1",
        imageUrl = "imageUrl1",
        date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -2) },
        rows = 3,
        columns = 3
    )

    val mockedPuzzle2 = Puzzle(
        id = "2",
        imageUrl = "imageUrl2",
        date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) },
        rows = 3,
        columns = 3
    )

    val mockedPuzzle3 = Puzzle(
        id = "3",
        imageUrl = "imageUrl3",
        date = Calendar.getInstance(),
        rows = 3,
        columns = 3
    )

    val creations = listOf(mockedPuzzle1, mockedPuzzle2, mockedPuzzle3)

    MaterialTheme {
        CreationContent(creations = creations)
    }
}

@Preview(showBackground = true)
@Composable
fun FloatingActionButtonsPreview() {
    CreationFloatingActionButtons()
}
