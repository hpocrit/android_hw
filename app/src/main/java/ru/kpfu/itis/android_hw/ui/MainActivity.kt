package ru.kpfu.itis.android_hw.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.kpfu.itis.android_hw.R
import ru.kpfu.itis.android_hw.ui.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainFragment())
            .commit()
    }


}