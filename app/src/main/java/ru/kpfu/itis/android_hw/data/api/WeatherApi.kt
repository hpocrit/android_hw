package ru.kpfu.itis.android_hw.data.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.kpfu.itis.android_hw.BuildConfig
import ru.kpfu.itis.android_hw.data.response.WeatherResponse

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = BuildConfig.openWeatherApiKey
    ) : WeatherResponse
}