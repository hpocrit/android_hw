package ru.kpfu.itis.android_hw.util

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ru.kpfu.itis.android_hw.MainActivity
import ru.kpfu.itis.android_hw.data.di.ServiceLocator

class InceptionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initDatabase(ctx = this)

    }
}