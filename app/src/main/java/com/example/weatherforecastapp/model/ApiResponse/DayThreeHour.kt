package com.example.weatherforecastapp.model.ApiResponse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * response class for weather api
 */
class DayThreeHour(
    @field:SerializedName("dt") val epochTime: Long, @field:Expose
    @field:SerializedName("main") val dayMetrics: DayThreeHourParams
) {
    fun dayEpochToDate(): Long {
        return epochTime
    }

    fun temperature(): Double {
        return dayMetrics.temperature()
    }

    fun humidity(): Double {
        return dayMetrics.humidity()
    }

}