package ru.kpfu.itis.android_hw

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main)

            supportFragmentManager.beginTransaction()
                .add(R.id.container1_land, FirstFragment())
                .add(R.id.container2_land, FourthFragment())
                .commit()

        } else{
            setContentView(R.layout.activity_main)

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FirstFragment())
                .commit()

        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        MainActivity().recreate()
    }

}
