package com.example.weatherforecastapp.model

import com.google.gson.annotations.SerializedName

class DayParams(@field:SerializedName("temp") val temp : Double, @field:SerializedName("humidity") val humidity: Double) {
    fun temperature(): Double {
        return temp
    }
    fun humidity() : Double{
        return humidity
    }
}