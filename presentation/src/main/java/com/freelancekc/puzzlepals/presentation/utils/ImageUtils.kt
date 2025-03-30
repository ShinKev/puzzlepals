package com.freelancekc.puzzlepals.presentation.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

suspend fun getBitmapFromUrl(context: Context, imageUrl: String?): Bitmap? {
    if (imageUrl == null) return null

    return withContext(Dispatchers.IO) {
        if (imageUrl.startsWith("R.drawable.")) {
            // Handle resource ID format
            getBitmapFromResource(context, imageUrl)
        } else {
            // Handle URI format
            getBitmapFromUri(context, imageUrl.toUri())
        }
    }
}

private fun getBitmapFromResource(context: Context, imageUrl: String): Bitmap? {
    val resourceName = imageUrl.substringAfter("R.drawable.")
    val resourceId = context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )

    return if (resourceId != 0) {
        try {
            BitmapFactory.decodeResource(context.resources, resourceId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else null
}

private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    val contentResolver: ContentResolver = context.contentResolver
    var inputStream: InputStream? = null

    return try {
        inputStream = contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}

@Composable
fun rememberGalleryLauncher(
    onImageSelected: (Uri) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    uri?.let(onImageSelected)
}

@Composable
fun rememberCameraLauncherWithPermission(
    onImageSelected: (Uri) -> Unit
): androidx.activity.result.ActivityResultLauncher<String> {
    val context = LocalContext.current
    val photoUri = remember {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            context.cacheDir
        )
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onImageSelected(photoUri)
        }
    }

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(photoUri)
        }
    }
} 