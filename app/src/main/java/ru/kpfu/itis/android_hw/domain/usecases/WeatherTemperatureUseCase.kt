package ru.kpfu.itis.android_hw.domain.usecases

import ru.kpfu.itis.android_hw.data.repositories.WeatherRepository
import ru.kpfu.itis.android_hw.di.Container

class WeatherTemperatureUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(city: String): String {
        return Container.weatherRepository.getTempByCity(city)
    }
}