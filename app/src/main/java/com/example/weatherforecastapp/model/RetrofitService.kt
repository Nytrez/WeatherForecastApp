package com.example.weatherforecastapp.model

import com.example.weatherforecastapp.model.ApiResponse.Days
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("/data/2.5/forecast")
    fun getForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") typeOfUnits: String = "metric",
        @Query("appid") userAppId: String = "4391014971a63498ee6be88eb3b17b44"

    ): Observable<Days>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }
}

object BranchFinderService {

    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(OpenWeatherMapApi.BASE_URL)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(OpenWeatherMapApi::class.java)

    fun buildService(): OpenWeatherMapApi {
        return retrofit
    }

}
