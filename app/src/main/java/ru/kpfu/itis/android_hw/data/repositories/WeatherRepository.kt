package ru.kpfu.itis.android_hw.data.repositories

import ru.kpfu.itis.android_hw.data.api.WeatherApi

class WeatherRepository(
    private val api: WeatherApi,
) {

    suspend fun getTempByCity(city: String) : String {
        return api.getWeather(city).main.temp.toString()
    }

    suspend fun getIconIdByCity(city: String) : String {
        var id = ""
        api.getWeather(city).weather.firstOrNull()?.also {
            id = it.icon
        }
        return id
    }
}