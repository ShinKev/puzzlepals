package com.freelancekc.puzzlepals.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

class ImagePicker(private val context: Context) {
    private var onImageSelected: ((Uri) -> Unit)? = null

    fun pickFromGallery(onImageSelected: (Uri) -> Unit) {
        this.onImageSelected = onImageSelected
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        context.startActivity(intent)
    }

    fun pickFromCamera(onImageSelected: (Uri) -> Unit) {
        this.onImageSelected = onImageSelected
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        context.startActivity(intent)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == android.app.Activity.RESULT_OK) {
            val uri = when (requestCode) {
                GALLERY_REQUEST_CODE -> data?.data
                CAMERA_REQUEST_CODE -> {
                    // In a real app, we would save the camera image to a file and get its URI
                    data?.data
                }
                else -> null
            }
            uri?.let { onImageSelected?.invoke(it) }
        }
    }

    companion object {
        const val GALLERY_REQUEST_CODE = 1001
        const val CAMERA_REQUEST_CODE = 1002
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