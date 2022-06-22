package com.example.weatherforecastapp.model.ApiResponse

import com.google.gson.annotations.SerializedName

/**
 * response class "body" of DayOfWeek for weather api
 */
class DayThreeHourParams(@field:SerializedName("temp") val temp: Double, @field:SerializedName("humidity") val humidity: Double) {
    fun temperature(): Double {
        return temp
    }

    fun humidity(): Double {
        return humidity
    }
}