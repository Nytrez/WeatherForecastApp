package com.example.weatherforecastapp.model

import com.example.weatherforecastapp.utils.epochToDate
import com.example.weatherforecastapp.utils.epochToHour
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DayOfWeek (@field:SerializedName("dt") val epochTime : Long, @field:SerializedName("dt_txt") val _date_txt : String, @field:Expose
@field:SerializedName("main") val dayMetrics : DayParams
){
    fun dayEpochToDate() : Long{
        return epochTime
    }

    fun epochHour() : Int {
        return epochToHour(epochTime)
    }

    fun temperature() : Double {
        return dayMetrics.temperature()
    }

    fun humidity() : Double {
        return dayMetrics.humidity()
    }

    fun dayTime() : String{
        return "${epochToDate(epochTime)} + $_date_txt"
    }
}