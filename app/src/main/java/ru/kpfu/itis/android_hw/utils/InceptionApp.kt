package ru.kpfu.itis.android_hw.utils

import android.app.Application
import ru.kpfu.itis.android_hw.di.Container

class InceptionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Container.initException(this)
        Container.initUseCases()
    }
}