package com.freelancekc.puzzlepals.presentation.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

private const val QUARTER_OF_FULL_ROTATION = 90f
private const val HALF_OF_FULL_ROTATION = 180f
private const val THREE_QUARTER_OF_FULL_ROTATION = 270f

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
        val bitmap = BitmapFactory.decodeStream(inputStream)
        rotateBitmap(bitmap, uri, context)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}

private fun rotateBitmap(bitmap: Bitmap, uri: Uri, context: Context): Bitmap {
    // Get EXIF orientation using ContentResolver
    val contentResolver: ContentResolver = context.contentResolver
    var inputStream: InputStream? = null

    return try {
        inputStream = contentResolver.openInputStream(uri)
        val exif = ExifInterface(inputStream!!)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        // Create matrix for rotation
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(QUARTER_OF_FULL_ROTATION)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(HALF_OF_FULL_ROTATION)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(THREE_QUARTER_OF_FULL_ROTATION)
        }

        // Apply rotation if needed
        if (!matrix.isIdentity) {
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            bitmap
        }
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