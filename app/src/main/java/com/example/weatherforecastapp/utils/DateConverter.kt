package com.example.weatherforecastapp.utils

import java.time.Instant
import java.time.ZoneId

/** Function to convert epoch time to an hour of local time
 *  @param time Epoch time in milliseconds
 *  @return local time hour of the day
 */
fun epochToHour(time: Long): Int {
    val dt = Instant.ofEpochSecond(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return dt.hour
}

/** Function to convert epoch time to a day of Month of local time
 *  @param time Epoch time in milliseconds
 *  @return day of month of local time
 */
fun epochToDay(time: Long): Int {
    // no need for null checking
    val dt = Instant.ofEpochSecond(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return dt.dayOfMonth
}

/** Function to convert epoch time to the given day name of local time
 *  @param time Epoch time in milliseconds
 *  @return Day name of the day
 */
fun epochToDayName(time: Long): String {
    val dt = Instant.ofEpochSecond(time)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    return dt.dayOfWeek.name
}
