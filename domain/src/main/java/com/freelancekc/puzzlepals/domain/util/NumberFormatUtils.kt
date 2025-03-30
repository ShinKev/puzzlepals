package com.freelancekc.puzzlepals.domain.util

object NumberFormatUtils {
    /**
     * Formats a number into a human-readable string representation.
     * - For numbers >= 1,000,000, returns "x.xxM" format
     * - For numbers >= 1,000, returns "x.xxK" format
     * - For numbers < 1,000, returns the number as is
     *
     * @param number The number to format
     * @return A formatted string representation of the number
     */
    fun formatLikesCount(number: Long): String {
        return when {
            number >= 1_000_000 -> String.format("%.2fM", number / 1_000_000.0)
            number >= 1_000 -> String.format("%.2fK", number / 1_000.0)
            else -> number.toString()
        }
    }
} 