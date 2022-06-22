package com.example.weatherforecastapp.model.ApiResponse

import com.example.weatherforecastapp.model.NextDays
import com.example.weatherforecastapp.utils.epochToDay
import com.example.weatherforecastapp.utils.epochToHour
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Days(@field:Expose @field:SerializedName("list") val weekDays: List<DayThreeHour>) {

    /** Function for parsing the api response into a list of NexDays of length 5
     * Api by default returns weather forecast in 3 hour intervals
     * @return List of NextDays of length 5
     */
    fun fiveDays(): List<NextDays> {
        var epochTime = 0L
        val morning = mutableListOf<Double>()
        val day = mutableListOf<Double>()
        val night = mutableListOf<Double>()
        var humidity = 0.0

        val currDayTmps = mutableListOf<Double>()
        val nextDaysValues = mutableListOf<NextDays>()

        // taking values from the same days (each day on average has 8 instances of 3 hour intervals)
        for ((i, it) in weekDays.withIndex()) {
            if ((epochToDay(epochTime) != epochToDay(it.dayEpochToDate())) or (i == weekDays.size - 1)) {
                if (i == weekDays.size - 1) {
                    //if its the 5th days last interval then we need to add its values before adding the to the NextDays List
                    humidity += it.humidity()
                    currDayTmps.add(it.temperature())
                }
                if ((epochTime != 0L)) {
                    // taking average,minimum and maximum of the days temperatures
                    val minTmp = currDayTmps.min()
                    val maxTmp = currDayTmps.max()
                    val avgTmp = currDayTmps.average()

                    // taking the mode value of the temperatures
                    val (modeTmp, _) = currDayTmps.groupingBy { it }.eachCount().maxByOrNull { it.value }!!
                    humidity /= currDayTmps.size

                    val morningTmp = if (morning.size > 0) morning.average() else Double.NaN
                    val dayTmp = if (day.size > 0) day.average() else Double.NaN
                    val nightTmp = if (night.size > 0) night.average() else Double.NaN

                    val currDay = NextDays(epochTime, morningTmp, dayTmp, nightTmp, humidity, minTmp, maxTmp, avgTmp, modeTmp)
                    nextDaysValues.add(currDay)
                }
                // clearing values for the next day
                epochTime = it.dayEpochToDate()
                currDayTmps.clear()
                morning.clear()
                day.clear()
                night.clear()
                humidity = it.humidity()
                currDayTmps.add(it.temperature())

            } else {
                // values for morning, day and night temperatures
                when (epochToHour(it.dayEpochToDate())) {
                    in 5..11 -> {
                        morning.add(it.temperature())
                    }
                    in 11..17 -> {
                        day.add(it.temperature())
                    }
                    in 17..23 -> {
                        night.add(it.temperature())
                    }
                }
                humidity += it.humidity()
                currDayTmps.add(it.temperature())
            }

        }

        return nextDaysValues.take(5)
    }
}

