package com.example.weatherforecastapp.model

import android.util.Log
import com.example.weatherforecastapp.utils.epochToDate
import com.example.weatherforecastapp.utils.epochToHour
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeekDays(@field:Expose @field:SerializedName("list") val weekDays : List<DayOfWeek>) {

    fun print(){
        for (i in weekDays){
            Log.d("TEST", "${i.temperature()}")
        }
    }

    fun fiveDays() : List<NextDays>{
        var epochTime = 0L
        var morning = Double.MIN_VALUE
        var day = Double.MIN_VALUE
        var night = Double.MIN_VALUE
        var humidity = 0.0
        var i = 0

        val currDayTmps = mutableListOf<Double>()
        val nextDaysValues = mutableListOf<NextDays>()

        for(it in weekDays){
            if(epochToDate(epochTime) != epochToDate(it.dayEpochToDate())){
              if(epochTime != 0L) {
                  val minTmp = currDayTmps.min()
                  val maxTmp = currDayTmps.max()
                  val avgTmp = currDayTmps.average()
                  val (modeTmp, _) = currDayTmps.groupingBy { it }.eachCount().maxByOrNull { it.value }!!
                  humidity /= currDayTmps.size

                  val currDay = NextDays(epochTime, morning, day, night, humidity, minTmp, maxTmp, avgTmp, modeTmp)
                  nextDaysValues.add(currDay)
              }

                epochTime = it.dayEpochToDate()
                currDayTmps.clear()
                morning = Double.MIN_VALUE
                day = Double.MIN_VALUE
                night = Double.MIN_VALUE
                humidity = 0.0

            } else {
                when (epochToHour(it.dayEpochToDate())) {
                    in 6..8 -> {
                        morning = it.temperature()
                    }
                    in 12..14 -> {
                        day = it.temperature()
                    }
                    in 21..23 -> {
                        night = it.temperature()
                    }
                }
                humidity += it.humidity()
                currDayTmps.add(it.temperature())
            }

        }

        // for the last day
        val minTmp = currDayTmps.min()
        val maxTmp = currDayTmps.max()
        val avgTmp = currDayTmps.average()
        val (modeTmp, _) = currDayTmps.groupingBy { it }.eachCount().maxByOrNull { it.value }!!
        humidity /= currDayTmps.size

        val currDay = NextDays(epochTime, morning, day, night, humidity, minTmp, maxTmp, avgTmp, modeTmp)
        nextDaysValues.add(currDay)

        return nextDaysValues
    }
}

