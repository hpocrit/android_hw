package ru.kpfu.itis.android_hw.di

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.android_hw.BuildConfig
import ru.kpfu.itis.android_hw.data.handlers.ExceptionHandlerDelegate
import ru.kpfu.itis.android_hw.data.api.WeatherApi
import ru.kpfu.itis.android_hw.data.repositories.WeatherRepository
import ru.kpfu.itis.android_hw.domain.usecases.WeatherIconUseCase
import ru.kpfu.itis.android_hw.domain.usecases.WeatherTemperatureUseCase
import ru.kpfu.itis.android_hw.utils.ResManager

object Container {

    lateinit var exceptionHandlerDelegate: ExceptionHandlerDelegate
    lateinit var weatherTemperatureUseCase: WeatherTemperatureUseCase
    lateinit var iconUseCase: WeatherIconUseCase

    val weatherRepository by lazy {
        WeatherRepository(weatherApi)
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }



    private val retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.OPEN_WEATHER_BASE_URL)
            .build()
    }

    fun initException(ctx: Context){
        exceptionHandlerDelegate = ExceptionHandlerDelegate(ResManager(ctx))
    }

    fun initUseCases() {
        weatherTemperatureUseCase = WeatherTemperatureUseCase(weatherRepository)
        iconUseCase = WeatherIconUseCase(weatherRepository)
    }

    val weatherApi = retrofit.create(WeatherApi::class.java)


}