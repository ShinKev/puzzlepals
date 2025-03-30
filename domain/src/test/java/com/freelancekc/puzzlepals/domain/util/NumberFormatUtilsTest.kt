package com.freelancekc.puzzlepals.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test

class NumberFormatUtilsTest {

    @Test
    fun `formatLikesCount formats millions correctly`() {
        // Given
        val number = 1_321_555L

        // When
        val result = NumberFormatUtils.formatLikesCount(number)

        // Then
        assertEquals("1.32M", result)
    }

    @Test
    fun `formatLikesCount formats thousands correctly`() {
        // Given
        val number = 358_120L

        // When
        val result = NumberFormatUtils.formatLikesCount(number)

        // Then
        assertEquals("358.12K", result)
    }

    @Test
    fun `formatLikesCount formats numbers under thousand correctly`() {
        // Given
        val number = 999L

        // When
        val result = NumberFormatUtils.formatLikesCount(number)

        // Then
        assertEquals("999", result)
    }

    @Test
    fun `formatLikesCount formats exact millions correctly`() {
        // Given
        val number = 1_000_000L

        // When
        val result = NumberFormatUtils.formatLikesCount(number)

        // Then
        assertEquals("1.00M", result)
    }

    @Test
    fun `formatLikesCount formats exact thousands correctly`() {
        // Given
        val number = 1_000L

        // When
        val result = NumberFormatUtils.formatLikesCount(number)

        // Then
        assertEquals("1.00K", result)
    }

    @Test
    fun `formatLikesCount formats zero correctly`() {
        // Given
        val number = 0L

        // When
        val result = NumberFormatUtils.formatLikesCount(number)

        // Then
        assertEquals("0", result)
    }
} 