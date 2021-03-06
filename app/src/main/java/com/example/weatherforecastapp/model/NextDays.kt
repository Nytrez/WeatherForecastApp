package com.example.weatherforecastapp.model

/**
 * Data class created for recyclerView items containing weather data
 */
data class NextDays(
    val epochTime: Long, val morningTmp: Double, val dayTmp: Double, val nightTmp: Double, val humidity: Double, val
    minTmp: Double, val maxTmp: Double, val avgTmp: Double, val modeTmp: Double, var isExpanded: Boolean = false
)