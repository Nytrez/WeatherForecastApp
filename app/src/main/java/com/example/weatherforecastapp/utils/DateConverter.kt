package com.example.weatherforecastapp.utils

import java.time.Instant
import java.time.ZoneId


fun epochToDate(time: Long): String {
    // no need for null checking
    val dt = Instant.ofEpochSecond(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return "${dt.dayOfMonth}.${dt.month}.${dt.year}"
}

fun epochToHour(time: Long): Int {
    // no need for null checking
    val dt = Instant.ofEpochSecond(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return dt.hour
}
