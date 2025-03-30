package com.freelancekc.puzzlepals.domain.util

import java.util.Calendar

private const val THREE_DAYS_BEFORE = -3

fun Long.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = this@toCalendar
    }
}

fun Calendar.isOlderThanThreeDays(): Boolean {
    val twoDaysAgo = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, THREE_DAYS_BEFORE)
    }
    return this.timeInMillis < twoDaysAgo.timeInMillis
}

fun Calendar.setToMidnight() {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}
