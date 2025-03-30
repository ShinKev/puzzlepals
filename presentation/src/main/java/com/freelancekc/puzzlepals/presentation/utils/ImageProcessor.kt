package com.freelancekc.puzzlepals.presentation.utils

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Size
import kotlin.math.roundToInt

object ImageProcessor {
    fun scaleAndCropBitmap(
        bitmap: Bitmap,
        targetSize: Size
    ): Bitmap {
        val imageAspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val containerAspectRatio = targetSize.width / targetSize.height

        val (displayedWidth, displayedHeight) = if (imageAspectRatio > containerAspectRatio) {
            // Image is wider than container
            val height = targetSize.height
            val width = height * imageAspectRatio
            width to height
        } else {
            // Image is taller than container
            val width = targetSize.width
            val height = width / imageAspectRatio
            width to height
        }

        // Create a scaled bitmap that matches the displayed size
        val scaledBitmap = Bitmap.createScaledBitmap(
            bitmap,
            displayedWidth.toInt(),
            displayedHeight.toInt(),
            true
        )

        // Calculate crop offsets
        val cropOffsetX = (displayedWidth - targetSize.width) / 2
        val cropOffsetY = (displayedHeight - targetSize.height) / 2

        // Create a cropped bitmap
        return Bitmap.createBitmap(
            scaledBitmap,
            cropOffsetX.roundToInt(),
            cropOffsetY.roundToInt(),
            targetSize.width.toInt(),
            targetSize.height.toInt()
        )
    }
} 